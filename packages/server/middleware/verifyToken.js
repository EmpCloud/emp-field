import Response from '../response/response.js';
import config from 'config';
import jwt from 'jsonwebtoken';
import { UserAccess } from '../resources/utils/helpers/userRoutes.helper.js';

async function verifyToken(req, res, next) {
    try {
        const token = JSON.stringify(req.header('x-access-token'));
        if (!token) return res.status(401).send(Response.tokenFailResp('Access token is required '));

        jwt.verify(JSON.parse(token), config.get('token_secret'), async (_error, userData) => {
            if (userData != null) {
                if (userData?.userData?.memberType == 0) {
                    let route=req?.originalUrl;
                    let [mainRoute, id] = route.split('?');
                    let path = id ? mainRoute : route
                    const found = UserAccess.includes(path);
                    if (!found) return res.status(403).send(Response.accessDeniedResp('Access Denied for this API '));
                }

                const result = {
                    state: true,
                    userData,
                };
                req.verified = result;
                next();
            } else {
                return res.status(401).send(Response.tokenFailResp('Invalid access token....'));
            }
        });
    } catch (e) {
        return res.status(401).send(Response.tokenFailResp('Invalid access token'));
    }
}

export default verifyToken;
