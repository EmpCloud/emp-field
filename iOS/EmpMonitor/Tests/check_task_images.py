"""Check saved task-image identity and attachment preservation in Update Task."""

from pathlib import Path
import subprocess
import tempfile


app = Path(__file__).parents[1] / "EmpMonitor"
models = ["FilterTaskListResponseModel.swift", "CreateTaskRequestModel.swift", "UpdateTaskRequestModel.swift", "UpdateTaskStatusRequestModel.swift"]
source = "import Foundation\n" + "\n".join((app / "Models/TaskScreenModels" / file).read_text() for file in models)
view = (app / "Views/HomeScreens/TabMainScreen/TaskViews/TaskRescheduleSheetView.swift").read_text()
assignments = "\n".join(line.strip() for line in view.splitlines() if "updateTaskViewModel.files =" in line or "updateTaskViewModel.images =" in line)
assert assignments.count("updateTaskViewModel.") == 2
source += r'''
struct Attachments: Codable {
    var files: [DocFile] = []
    var images: [SelectedImage] = []
}
func preserving(_ selectedTask: FilterTaskListResponseData?) -> Attachments {
    var updateTaskViewModel = Attachments()
'''
source += assignments + r'''
    return updateTaskViewModel
}
var images: [[String: Any]] = []
for index in 1...4 {
    let suffix = String(index)
    let image: [String: Any] = [
        "_id": "image-" + suffix,
        "url": "https://example.test/photo-" + suffix + ".jpg",
        "description": "Photo " + suffix
    ]
    images.append(image)
}
let files: [[String: Any]] = [["_id": "file-1", "url": "https://example.test/document.pdf"]]
let fixture: [String: Any] = ["_id": "task-1", "clientId": "client-1", "orgId": "org-1", "taskName": "Task", "emp_id": "employee-1", "date": "2026-10-12", "start_time": "09:00:00", "end_time": "17:00:00", "taskDescription": "Task", "taskApproveStatus": 1, "tagLogs": [], "files": files, "images": images, "taskVolume": 1, "createdAt": "", "updatedAt": "", "__v": 0]
let selectedTask = try JSONDecoder().decode(FilterTaskListResponseData.self, from: JSONSerialization.data(withJSONObject: fixture))
assert(Set(selectedTask.images.map { $0.id }).count == 4)
assert(Set(selectedTask.images.map { $0.url }).count == 4)
let attachments = preserving(selectedTask)
let request = UpdateTaskRequestModel(taskID: selectedTask.id, clientID: selectedTask.clientID, taskName: selectedTask.taskName, startTime: selectedTask.startTime, endTime: selectedTask.endTime, taskDescription: selectedTask.taskDescription, date: selectedTask.date, files: attachments.files, images: attachments.images, value: TaskValue(amount: 0, currency: "INR"), taskVolume: selectedTask.taskVolume, tagLogs: [])
let encoded = try JSONSerialization.jsonObject(with: JSONEncoder().encode(request)) as! [String: Any]
assert(NSDictionary(dictionary: ["files": encoded["files"]!, "images": encoded["images"]!]).isEqual(to: ["files": files, "images": images]))
let empty = preserving(nil)
assert(empty.images.isEmpty && empty.files.isEmpty)
print("Task image identity and update-preservation checks passed.")
'''

with tempfile.TemporaryDirectory(prefix="emp-task-images-check-") as scratch:
    directory = Path(scratch)
    swift = directory / "main.swift"
    swift.write_text(source)
    binary = directory / "checks"
    subprocess.run(["xcrun", "swiftc", "-module-cache-path", str(directory / "cache"), str(swift), "-o", str(binary)], check=True)
    subprocess.run([str(binary)], check=True)
