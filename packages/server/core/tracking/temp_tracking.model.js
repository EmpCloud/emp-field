import mongoose from "mongoose";
const tempTrackSchema = new mongoose.Schema({
    orgId:{type:String, required:true},
    emp_id:{type:String, required:true},
    geologs:[{
        time:{ type:String},
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
tempTrackSchema.index({ orgId: 1 });
tempTrackSchema.index({ emp_id: 1 });
tempTrackSchema.index({ createdBy: 1 });
export default mongoose.model("tempTrackSchema",tempTrackSchema);