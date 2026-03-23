import mongoose from "mongoose";
const clientSchema=new mongoose.Schema({
    clientName: { type: String, required: true },
    orgId: { type: String },
    emp_id: { type: String },
    emailId:{type:String},
    contactNumber: {type: String, default:null},
    clientProfilePic:{type: String ,default:null},
    countryCode: { type: String, default:null},
    address1: { type: String, required: true }, 
    address2: { type: String,default: null },    
    city: { type: String }, 
    state: { type: String }, 
    country: { type: String, required: true }, 
    zipCode: { type: String, max: 6 ,default:null},
    latitude:  { type: String, required: true }, 
    longitude:  { type: String, required: true }, 
    clientType:{type:Number,default:0},//0:Active, 1:Suspended, 2:Expired
    category: {type: String, required:   true} ,		
    status: {type: Number,default:0},//0:Active, 1:Suspended, 2:Deleted
    assignedEmployees: { type: mongoose.Schema.Types.ObjectId, ref: 'userSchema' },
    createdBy:{type:String},	
    updatedBy:{type:String}					
},
    { timestamps: true }
)
export default mongoose.model('clientSchema', clientSchema);