import mongoose from "mongoose";
import trackModel from "../tracking/track.model.js";
import moment from "moment";

const transportSchema = new mongoose.Schema({
    orgId:{type:String, required:true},
    emp_id:{type:String, required:true},
    currentRadius:{type:Number,default:10},
    currentFrequency:{type: Number, default:25},
    currentMode:{type: String,default:"bike" },
    defaultConfig: { type: Array,default:[] },
},
{ timestamps: true }
)

transportSchema.pre('findOneAndUpdate', async function (next) {
    const update = this.getUpdate();
    const today = moment().format('YYYY-MM-DD');

    if (update.$set && (update.$set.currentFrequency !== undefined || update.$set.currentMode !== undefined)) {
        const existingDoc = await this.model.findOne(this.getQuery());

        let data = {};

        if (existingDoc && existingDoc.currentFrequency !== update.$set.currentFrequency) {
            data.currentFrequency = update.$set.currentFrequency;
        }

        if (existingDoc && existingDoc.currentMode !== update.$set.currentMode) {
            data.currentMode = update.$set.currentMode;
        }

        if (Object.keys(data).length > 0) {
            
            let updatedData = await trackModel.findOneAndUpdate(
                { emp_id: existingDoc.emp_id, orgId: existingDoc.orgId ,date: today},
                { $set: data },
                { returnDocument: 'after' }
            );
        }
    }

    next();
});




export default mongoose.model("transportSchema",transportSchema);