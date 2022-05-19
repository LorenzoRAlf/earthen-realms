var mongoose = require('mongoose');

var WeaponSchema = new mongoose.Schema({
	weaponId:String,
    weaponName:String,
    damageType:String,
    baseDamage:Number,
    weaponSpeed:Number
});

module.exports = mongoose.model('Weapons', WeaponSchema, 'Weapons');