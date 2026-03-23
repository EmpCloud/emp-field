import mongoose from "mongoose";
const tempTestTrackSchema = new mongoose.Schema({
    orgId:{type:String, required:true},
    emp_id:{type:String, required:true},
    date:{type:String},
    geologs:[{
        time:{ type:String,default:null},
        latitude:{type:Number},
        longitude:{type:Number},
        status:{type:Number,default:0},
        taskId: { type: mongoose.Schema.Types.ObjectId, ref: 'Task', default: null } 
    }],
    createdBy:{type:String},	
    updatedBy:{type:String}
},
{ timestamps: true }

)
tempTestTrackSchema.index({ orgId: 1 });
tempTestTrackSchema.index({ emp_id: 1 });
tempTestTrackSchema.index({ createdBy: 1 });
export default mongoose.model("tempTestTrackSchema",tempTestTrackSchema);