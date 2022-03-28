import account from './models/account'
import permission from "./models/permission";
import user from "./models/user";
import rights from "./models/role";
import category from "./models/category";

export default {
    ...account,
    ...permission,
    ...user,
    ...rights,
    ...category

}