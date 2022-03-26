import account from './models/account'
import permission from "./models/permission";
import user from "./models/user";
import rights from "./models/role";

export default {
    ...account,
    ...permission,
    ...user,
    ...rights,


}