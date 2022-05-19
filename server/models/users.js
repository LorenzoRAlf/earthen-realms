var mongoose = require('mongoose');

var UserSchema = new mongoose.Schema({
	userId:String,
    username:String,
    password:String,
    weaponList:Array,
    armorList:Array,
    coins:Number,
    points:Number
});

module.exports = mongoose.model('Users', UserSchema, 'Users');