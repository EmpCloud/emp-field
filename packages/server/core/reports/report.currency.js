import mongoose from 'mongoose'

const currencySchema = new mongoose.Schema({

    currencyId: { type: String, unique: true, default: 'latest' }, 
    
    result: { type: String, required: true },

    documentation: { type: String, required: true },

    terms_of_use: { type: String, required: true },

    time_last_update_unix: { type: Number, required: true },

    time_last_update_utc: { type: String, required: true },

    time_next_update_unix: { type: Number, required: true },

    time_next_update_utc: { type: String, required: true },

    base_code: { type: String, required: true },

    conversion_rates: {
        type: Map,
        of: Number, 
        required: true
    },


}, { timestamps: true });

export default mongoose.model('currency',currencySchema)