import { Check } from 'lucide-react';

const StatusTimeLine = ({ stagesWithColours, allTags }) => {
  // Get a list of tagNames from stagesWithColours for easier comparison
  const stageNames = stagesWithColours.map(stage => stage.tagName);

  // Find the index of the first matching tag in allTags that is also in stagesWithColours
  const firstMatchIndex = allTags.findIndex(tag =>
    stageNames.includes(tag.tagName)
  );

  // Helper function to check if the stage should be completed
  const isStageCompleted = (index, tagName) => {
    // Check if the tagName exists in the stagesWithColours or if it comes before the first match
    return (
      stageNames.includes(tagName) ||
      (firstMatchIndex !== -1 && index <= firstMatchIndex)
    );
  };

  if (!allTags || allTags.length === 0) {
    return null;
  }

  let taskdashes = allTags.length - 1;

  return (
    <div className="status_timeline_container__ flex gap-2 flex-col mb-3">
      <p className="text-xs font-semibold mt-2 sm:mt-3">Task Stage</p>
      <div className="flex items-center bg-white justify-between w-full overflow-x-auto 3xl:overflow-hidden 3xl:gap-0 2xl:gap-3 xl:gap-3 gap-2 relative  2xl:px-10 3xl:px-5 px-8 xl:px-[14px] py-2 pt-3 3xl:w-full rounded-sm sm:w-fit lg:w-full">
        {/* Line connecting all elements */}
        <div className="absolute top-[33%] left-9 right-[4.5rem] border-t-2 border-dotted border-[#6A6AEC] transform -translate-y-1/2 linedashabs"></div>

        {allTags.map((tag, index) => (
          <div
            key={index}
            className="flex flex-row items-center justify-center z-10 2xl:mr-[12px] 3xl:mr-[7px] lg:mr-[4px] relative">
            <div className="flex flex-col items-center">
              <span
                className="2xl:w-4 2xl:h-4 3xl:w-5 3xl:h-5 rounded-full flex justify-center items-center lg:w-[16px] lg:h-[16px] w-[12px] h-[12px]"
                style={{
                  backgroundColor: tag.color,
                }}>
                {isStageCompleted(index, tag.tagName) && (
                  <Check className="w-3 h-3 text-white " />
                )}
              </span>

              <span
                className="mt-2 2xl:text-xs xl:text-[13px] 3xl:text-[14px] text-xs font-semibold whitespace-nowrap"
                style={{ color: tag.color }}>
                {tag.tagName ?? ''}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default StatusTimeLine;
