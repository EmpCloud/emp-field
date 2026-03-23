import mongoose from 'mongoose';

const employeeLocation = new mongoose.Schema({
    orgId: { type: String, required: true }, 
    employeeId: { type: mongoose.Schema.Types.ObjectId, required: true, ref: 'userschemas' },  
    address: { type: String, default: null },
    latitude: { type: Number, default: null },
    longitude: { type: Number, default: null },
    range: { type: Number, default: 10 },
    geo_fencing: { type: Boolean, default: false },
    isMobEnabled: { type: Boolean, default: true },
    createdBy: { type: String },
    updatedBy: { type: String }
}, { timestamps: true });

export default mongoose.model('employeeLocation', employeeLocation);
