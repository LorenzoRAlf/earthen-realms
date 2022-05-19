var mongoose = require('mongoose');

var ArmorSchema = new mongoose.Schema({
	armorId:String,
    armorName:String,
    armorType:String,
    baseDefense:Number,
    baseSpeed:Number
});

module.exports = mongoose.model('Armors', ArmorSchema, 'Armors');