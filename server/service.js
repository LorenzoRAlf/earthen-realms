const { json } = require('express/lib/response');
const GameModel = require('./models/games');

module.exports = {
    getPlayers: function(gameId) {
        let gameData = null;
        if (1) {
            GameModel.findOne({"_id": gameId},
            function (err, game) {
                if (err) {
                    console.log(err);
                    data = {'status': 'ERROR', "message": err};
                    gameData = data;
                } else {
                    if (game == null) {
                        console.log("necesito arreglar esto");
                        data = {'status': 'ERROR', "message":"necesito arreglar esto"};
                        gameData = data;
                    } else {
                        console.log("Current players: "+game.players);
                        data = {status: 'OK', players: game.players};
                        gameData = data;
                    }
                }
            })
            return gameData
        }
    },

    setPlayers: function(gameID) {
        GameModel.findOneAndUpdate({"_id":gameID}, { $inc: {"players": 1 },
            function (err, game) {
                if (err) {
                    console.log('Error: '+ err);
                    data = {'status': 'ERROR', "message": err};
                    return data;
                } else {
                    console.log("Current players updated");
                    data = {'status': 'OK', "message": "Current players: "+ game.players};
                    return data;
                }
            }
        })
    }
}