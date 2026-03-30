import Response from '../../response/response.js';
import moment from 'moment';
import config from 'config';
import adminSchema from './../admin/admin.model.js';
import autoEmailReportValidation from './autoEmailReport.validation.js';
import autoReportModule from './autoEmailReport.model.js';
import { ObjectId } from 'mongodb';

class autoEmailReportService {

    async createAutoEmailReport(req, res) {
        const result = req?.verified;

        if (result?.state === true) {
            try {
                const data = req.body;
                const { orgId, emp_id } = result.userData.userData;

                const isAdminDataExist = await adminSchema.findOne({ orgId, emp_id });
                if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));

                const { value, error } = autoEmailReportValidation.sendReport(data);
                if (error) {
                    const errorMessage = error.details[0].context.message;
                    return res.send(Response.SuccessResp("Validation Failed", errorMessage));
                }
                

                data.orgId = orgId;

                const findReport = await autoReportModule.findOne({ reportsTitle: data.reportsTitle, orgId });
                if (findReport) return res.send(Response.FailResp("This title with report already present."));

                if (!Array.isArray(data.Recipients) || data.Recipients.length <= 0) {
                    return res.send(Response.FailResp("Minimum one user required."));
                }

                if (!Array.isArray(data.Content) || data.Content.length <= 0) {
                    return res.send(Response.FailResp("Minimum one Content required."));
                }

                if (!data.filter || Object.keys(data.filter).length <= 0) {
                    return res.send(Response.FailResp("Minimum one sendTo filter option required."));
                }

                // Create the report
                const createReport = await autoReportModule.create(data);
                if (createReport) {
                    // Initialize cron jobs if necessary
                    // cronJobActivity.initializeCronJobs();
                    return res.send(Response.SuccessResp("Information Stored successfully", createReport));
                } else {
                    return res.send(Response.FailResp("Error while creating data"));
                }
                
            } catch (error) {
                return res.send(Response.FailResp("Failed to store Data", error.message));
            }
        } else {
            res.send(result);
        }
    }

    async fetchReportDetails(req, res) {
        try {
            const result = req.verified;
            const {orderby,sort,searchQuery, } = req.query;
            let skip = req.query.skip || 0;
            let limit = req.query.limit || 10;
            const { orgId, emp_id ,email} = result.userData.userData;


            const sortBy = {};
            if (orderby === 'frequency') {
                sortBy['frequency.Daily'] = sort?.toString() === 'desc' ? -1 : 1;
                sortBy['frequency.Weekly'] = sort?.toString() === 'desc' ? -1 : 1;
                sortBy['frequency.Monthly'] = sort?.toString() === 'desc' ? -1 : 1;
            } else {
                sortBy[orderby || 'reportsTitle'] = sort?.toString() === 'desc' ? -1 : 1;
            }

            const isAdminDataExist = await adminSchema.findOne({ orgId, emp_id });
            if (!isAdminDataExist) return res.send(Response.FailResp(`Admin does not exist.`));

            let query = { orgId: orgId }
            if (searchQuery) {
                query.$or = [
                    { reportsTitle: new RegExp(searchQuery, 'i') },
                    { Recipients: { $elemMatch: { $regex: searchQuery, $options: 'i' } } }
                ];
                query.orgId = orgId
            }
            console.log(query,skip,limit);
            let autoReportFetch = await autoReportModule.find(query).select('reportsTitle frequency Recipients Content').sort(sortBy).skip(skip).limit(limit);
            let totalCount = await autoReportModule.countDocuments(query)

            if (autoReportFetch) {
                let data = {
                    totalCount,
                    autoReportFetch
                }
                res.send(Response.SuccessResp("Information Fetched successfully", data))
            } else {
                res.send(Response.FailResp("Error while fetching data"));
            }
        } catch (error) {
            return res.send(Response.FailResp("Failed to store Data", error.message));
        }
    }
    async updateReport(req, res) {
        try {
            const reportId = req.query.Id;
            const data = req.body;
            const result = req?.verified;
            const { orgId } = result.userData.userData;
    
            const { value, error } = autoEmailReportValidation.updateReport(data);
            if (error) {
                const errorMessage = error.details[0].message;
                return res.send(Response.SuccessResp("Validation Failed", errorMessage));
            }
    
            data.orgId = orgId;
    
            const findReport = await autoReportModule.findOne({ _id: new ObjectId(reportId), orgId: orgId });
            if (!findReport) {
                return res.send(Response.FailResp("Report not found."));
            }
    
            const reportName = await autoReportModule.findOne({ reportsTitle: data.reportsTitle, orgId: data.orgId });
            if (reportName) {
                return res.send(Response.FailResp("A report with this title already exists."));
            }
    
            const updateData = await autoReportModule.updateOne(
                { _id: new ObjectId(reportId) },
                { $set: data },
                { returnDocument: 'after' }
            );
    
            if (updateData.modifiedCount > 0) {
                // cronJobActivity.initializeCronJobs();
                res.send(Response.SuccessResp("Report updated successfully", data));
            } else {
                res.send(Response.FailResp("Error while updating report"));
            }
        } catch (error) {
            return res.send(Response.FailResp("Failed to update report", error.message));
        }
    }

    async deleteReport(req, res) {
        const result = req?.verified;
        if (result.state === true) {
            try {
                const reportId = req?.query?.Id;
                const deletedData = await autoReportModule.findOneAndDelete({ _id: reportId });
                let frequency = null;

                if (deletedData && deletedData.frequency && deletedData.frequency.length > 0) {
                  const freq = deletedData.frequency[0];
                  if (freq.Daily === 1 ){
                    frequency = "Daily";
                  }else if(freq.Weekly===1){
                    frequency="Weekly";
                  }else if(freq.Monthly===1){
                    frequency="Monthly";
                  }
                }
                if (deletedData) {
                //   cronJobActivity.deleteCronJob(reportId,frequency)
                    res.send(Response.SuccessResp("Data deleted successfully", deletedData))
                } else {
                    res.send(Response.FailResp("Error while deleting data"));
                }
            } catch (err) {
                return res.send(Response.FailResp("Failed to delete Data", err.message));
            }
        } else {
            res.send(result)
        }
    }    

}

export default new autoEmailReportService();
