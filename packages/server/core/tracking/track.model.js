import mongoose from "mongoose";
import Task from "../task/task.model.js";
const trackSchema = new mongoose.Schema({
    orgId:{type:String, required:true},
    emp_id:{type:String, required:true},
    distTravelled:{type:Number,default:0},
    date:{type:String},
    checkIn:{type:String,default:null},
    checkOut:{type:String,default:null},
    currentFrequency:{type: Number, default:25},
    currentMode:{type: String,default:"bike" },
    geologs:[{
        time:{ type:String,default:null},
        latitude:{type:Number},
        longitude:{type:Number},
        status:{type:Number,default:0},
        viewed:{type:Number,default:0},
        taskId: { type: mongoose.Schema.Types.ObjectId, ref: 'Task', default: null } 
    }],
    createdBy:{type:String},	
    updatedBy:{type:String}
},
{ timestamps: true }

)
trackSchema.index({ orgId: 1 });
trackSchema.index({ emp_id: 1 });
trackSchema.index({ createdBy: 1 });
export default mongoose.model("trackSchema",trackSchema);