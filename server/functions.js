const crypto = require('crypto');
const UserModel = require('./models/users');

module.exports = {
    //Funcion para generar token
    generateRandomString : function(num) {
        const characters ='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result= ' ';
        const charactersLength = characters.length;
        for ( let i = 0; i < num; i++ ) {
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
        }
        return result;
    },

    get_token: function(user) {
        if (user.session_token != '') {
        if (Date.now() < user.session_token_expiration_date) {
            return user.session_token;
        }
        }
        var random = Math.floor(Math.random() * 1000);
        var new_token = crypto.createHash('md5').update(user.username + user.password + random).digest('hex');
        var expiration_time = new Date(parseInt(Date.now()) + parseInt(1000000));
        
        UserModel.updateOne({username: user.username, password: user.password}, 
        {session_token_expiration_date: expiration_time});
        return new_token;
    }
}
