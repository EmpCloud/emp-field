import mongoose from 'mongoose'

const holidaySchema = new mongoose.Schema({

    name:{ type: String, required:true }, 
    date: { type:Date,required:true },  
    orgId:{type: String, required:true }

},{timestamps:true})


export default mongoose.model('holiday',holidaySchema)