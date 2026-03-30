import mongoose from 'mongoose'

const orgLocationSchema = new mongoose.Schema({

    orgId:{ type: String, required:true }, 
    locationName: { type:String,required:null },  
    address:{type: String, default: null },
    latitude:{type: String, default: null },
    longitude:{type: String, default: null },
    range:{type: Number, default: 10 },
    geo_fencing:{type: Boolean, default: false },
    isMobEnabled:{type: Boolean, default: true },
    global: { type: Array, default: [] },
    createdBy:{type:String},	
    updatedBy:{type:String}

},{timestamps:true})


export default mongoose.model('orgLocation',orgLocationSchema)