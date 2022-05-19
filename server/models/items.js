var mongoose = require('mongoose');

var ItemSchema = new mongoose.Schema({
	itemId:String,
    itemName:String,
    coinCost:Number
});

module.exports = mongoose.model('Items', ItemSchema, 'Items');