import mongoose from 'mongoose'

const leaveSchema = new mongoose.Schema({

    name:{ type: String, required:true }, 
    duration: { type:Number,required:true }, //1-yearly,2-half-yearly,3-quarterly,4-monthly
    no_of_days:{ type:Number,required:true },
    carry_forward: {type: Number, required:true}, //0-No, 1-Yes
    orgId:{type: String, required:true }

},{timestamps:true})


export default mongoose.model('leave',leaveSchema)