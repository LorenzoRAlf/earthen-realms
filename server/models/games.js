var mongoose = require('mongoose');

var GameSchema =  new mongoose.Schema({
	gameId:String,
    players:Number,
    status:String,
    player1Id:String,
    player2Id:String,
    player1Weapon:String,
    player2Weapon:String,
    player1Armor:String,
    player2Armor:String,
    winner:String

});

module.exports = mongoose.model('Games', GameSchema, 'Games');