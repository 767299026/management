import account from './models/account'
import permission from "./models/permission";
import user from "./models/user";
import rights from "./models/role";
import category from "./models/category";
import attribute from "./models/attribute";
import order from "./models/order";
import goods from "./models/goods";

export default {
    ...account,
    ...permission,
    ...user,
    ...rights,
    ...category,
    ...attribute,
    ...goods,
    ...order
}