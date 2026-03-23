import mongoose from 'mongoose'

const userTrackingSchema = new mongoose.Schema({
    userId :{ type: String, required: true },			//assigned_user\
    orgId : { type: String, required: true },
    trackingType: { type: String, required: true },		//login,logOut,InTask
    taskId: { type: String},	// required :true,only if “trackingType” is “InTask”
    date : { type: Date, default: Date.now()},
    latitude:  { type: String, required: true }, 
    longitude:  { type: String, required: true },
   },
    { timestamps: true }
)

export default mongoose.model('userTracking', userTrackingSchema);
