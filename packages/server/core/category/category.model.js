import mongoose from "mongoose";
const categorySchema=new mongoose.Schema({
    categoryName:{type:String},
    orgId: {type:String},
},
{timestamps:true}
)
export default  mongoose.model('categorySchema', categorySchema);