import mongoose from 'mongoose';

const orgSchema = new mongoose.Schema(
    {
        orgName: { type: String }
    },
    { timestamps: true }
);

export default mongoose.model('organization', orgSchema);
