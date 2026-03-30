import Response from "../../response/response.js";
import roleSchema from './role.model.js';


class roleService {
    async create(req, resp) {
        try {

            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            orgId = orgId.toString()
            const role = req?.body?.role;
            const roleExists = await roleSchema.findOne({ role: new RegExp(`^${role}$`, 'i'), orgId: orgId })

            if (roleExists) return resp.send(Response.FailResp('role already exists'))

            const resultData = await roleSchema.create({ role, orgId })

            if (resultData) return resp.send(Response.SuccessResp('Role created successfully', resultData))
            return resp.send(Response.FailResp('Error whilecreating roles'))

        } catch (err) {
           return resp.send(Response.FailResp('Something went wrong', err))
        }
    }

    async get(req, resp) {
        try {
            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            let searchQuery = req.query.searchQuery;
            let data = {
                orgId: orgId,
            };
            const fieldsToSearch = ['role','orgId']; 
            if (searchQuery) {
                    data.$or = fieldsToSearch.map(field => ({
                        [field]: new RegExp(searchQuery, 'i')
                    }));
            }

            let skip = req.query.skip || 0;
            let limit = req.query.limit || 10;
            const resultData = await roleSchema.find(data).limit(limit).skip(skip)

            if (resultData) return resp.send(Response.SuccessResp('Role fetched successfully', resultData))
            return resp.send(Response.FailResp('Error while fetching roles'))

        } catch (err) {
            return resp.send(Response.FailResp('Something went wrong', err))
        }
    }

    async update(req, resp) {
        try {

            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            const roleId = req?.query?.roleId.toString();
            // orgId =orgId.toString()
            const role = req?.body?.role;
            const roleData = await roleSchema.findOne({ _id: roleId, orgId: orgId })
            if (!roleData) return resp.send(Response.FailResp('No role found'))
            const resultData = await roleSchema.findOneAndUpdate({ _id: roleId }, { $set: { role: role } }, { returnDocument: 'after' })
            console.log(resultData)
            if (resultData) return resp.send(Response.SuccessResp('Role Updated successfully', resultData))
            return resp.send(Response.FailResp('Error while updating roles'))

        } catch (err) {
            return resp.send(Response.FailResp('Invalid roleId', err))
        }
    }

    async delete(req, resp) {
        try {

            const result = req.verified;
            let { orgId } = result?.userData?.userData;
            const roleId = req?.query?.roleId;

            const roleData = await roleSchema.findOne({ _id: roleId, orgId: orgId })

            if (!roleData) return resp.send(Response.FailResp('No role found'))
            const resultData = await roleSchema.findOneAndDelete({ _id: roleId }, { returnDocument: 'after' })

            if (resultData) return resp.send(Response.SuccessResp('Role Deleted successfully', resultData))
            return resp.send(Response.FailResp('Error while deleting roles'))

        } catch (err) {
           return resp.send(Response.FailResp('Something went wrong', err))
        }
    }
}

export default new roleService()