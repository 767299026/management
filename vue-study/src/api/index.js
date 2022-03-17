import account from './models/account'
import permission from "./models/permission";
import user from "./models/user";

export default {
    ...account,
    ...permission,
    ...user,
}