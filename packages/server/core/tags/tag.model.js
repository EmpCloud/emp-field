
import mongoose from "mongoose";

const { Schema } = mongoose;

const tagSchema = new Schema({
    orgId: { type: String, required: true },
    tagName: { type: String, required: true, unique: true, trim: true },
    tagDescription: { type: String, trim: true },
    color: { type: String, trim: true },
    createdBy: { type: Schema.Types.ObjectId, ref: 'userSchema', required: true },
    updatedBy: { type: Schema.Types.ObjectId, ref: 'userSchema' },
    isActive: { type: Boolean, default: true, required: true },
    order: { type: Number, min: 1, max: 10, required: true },
}, {
    timestamps: true
});

export default mongoose.model("Tag", tagSchema);
