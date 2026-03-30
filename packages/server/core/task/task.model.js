import mongoose from "mongoose";


const fileSchema = new mongoose.Schema({
    url: { type: String, default:null},
});

const imageSchema = new mongoose.Schema({
    url: { type: String , default:null},
    description: { type: String, default:null},
});

const tags = new mongoose.Schema({
    tagName: { type: String , default:null},
    time:{type:String,default:null}
});
const recurrenceDetails = new mongoose.Schema({
    TaskCycle:{type:Number,default:0},   //0:None , 1:Repeating Task  , 2:Daily task
    startDate:{type:String,default:null},
    endDate:{type:String,default:null},
    daysOfWeek:{ type: [Number], default: [] }  // Array of numbers (0-6 for Sunday-Saturday)
})

const { Schema } = mongoose;

const taskSchema = new Schema({
    clientId: { type: String },
    orgId: { type: String, required: true },
    taskName: { type: String, required: true },
    emp_id: { type: String, required: true },
    date: { type: String },
    start_time: { type: String },
    end_time: { type: String },
    taskDescription: { type: String },
    taskApproveStatus: { type: Number, default: 0 },
    empStartTime: { type: String, default: null },
    empEndTime: { type: String, default: null },
    tagLogs: [tags],
    createdBy: { type: String },
    updatedBy: { type: String },
    files: [fileSchema],
    images: [imageSchema],
    value: {
        currency: { type: String,default: null },
        amount: { type: Number, default: null},
        convertedAmountInUSD :{ type:Number, default:null }
    },
    taskVolume: { type: Number, default: null },
    recurrenceId: { type: String, default: null },
    recurrenceDetails: { 
        type: recurrenceDetails, 
        default: { TaskCycle: 0, startDate: null, endDate: null, daysOfWeek: [] } 
    }
}, { timestamps: true });
taskSchema.index({ orgId: 1 });
taskSchema.index({ taskName: 1 });
taskSchema.index({ createdAt: 1 });
taskSchema.index({ recurrenceId: 1 });
export default mongoose.model("taskSchema",taskSchema);