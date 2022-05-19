//==Dependencias
const express = require('express');
const app = express();
const router = express.Router();
//const PORT = process.env.PORT;
const PORT = 5000;
const mongoose = require('mongoose');
var cors = require('cors');
//Importamos los modelos
const UserModel = require('./models/users');
const WeaponModel = require('./models/weapons');
const GameModel = require('./models/games');
const functions = require('./functions');
var bodyParser = require("body-parser");
const { ConnectionPoolClosedEvent } = require('mongodb');
const { json } = require('express/lib/response');
const req = require('express/lib/request');

//==Conexion a la base de datos
/*process.env.DATABASE_URL*/
mongoose.connect('mongodb+srv://LorenzoRuiz:xIsLC3FMA24QK8ny@clustertestams.pvnhk.mongodb.net/FinalProject?retryWrites=true&w=majority')

//==Configuraciones
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'Authorization, X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Request-Method');
  res.header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, DELETE');
  res.header('Allow', 'GET, POST, OPTIONS, PUT, DELETE');
  next();
  
});
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

//=====GET=====

router.get('/', function (req, res) {
  res.send("Hello world!")
});


//GET users()
router.get('/users', function (req, res) {
  UserModel.find(function (err, users) {
    if (err) {
      res.send(err);
    }
    res.json({"status":"OK","user_list":users})
  });
  
});

//GET weapons()
router.get('/weapons', function (req, res) {
  WeaponModel.find(function (err, weapons) {
    if (err) {
      res.send(err);
    }
    res.json({"status":"OK","weapon_list":weapons})
  }); 
});

//==

//GET /api/login
//Aqui lo que haremos sera lo siguiente:
//1. Recoger los datos que nos llega con el req y comprobar si existen en la base de datos
//2. Generar el token y meterlo en la base de datos

router.get('/api/login',function(req,res){
  var username = req.query.username;
  var password = req.query.password;
  var status,message = "";
  var session_token = null;

  UserModel.find({ username: username, password: password }, function (err, docs) {
      if(docs.length == 0){
        status = "ERROR";
        message = "Wrong credentials";
        res.json({'status':status,'message':message})
      }else{
        status = "OK";
        session_token = functions.get_token(docs[0]);
        UserModel.updateOne({username: username, password: password}, 
          {session_token:session_token}, function (err, docs) {
          if (err){
              console.log(err)
          }
          else{
              console.log("Updated Docs : ", docs);
            }
        });
        res.send({'status':status,"session_token":session_token, "user":docs[0]})
      }
  });
});

//GET /api/logout
//Aqui solo tendremos que eliminar el token que nos llega de la base de datos
router.get('/api/logout',function(req,res){
  UserModel.updateOne({session_token: req.query.session_token }, 
    {session_token:""}, function (err, docs) {
    if (err){
      console.log(err);
      res.json({'status':"ERROR","message":"session_token is required"});
    }
    else{
      console.log("Updated Docs : ", docs);
      res.json({'status':"OK","message":"Session successfully closed."});
    }
  });
});

//Get Player
router.get('/player', async function(req, res){
  GameModel.findOne({"_id": req.query.gameId},
  function (err, game) {
    if (err) {
      console.log(err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      if (game == null) {
        console.log("necesito arreglar esto");
        res.json({'status': 'ERROR', "message":"necesito arreglar esto"});
      } else {
        console.log("Current players: "+game.players);
        res.json({status: 'OK', players: game.players});
      }
    }
  }) 
})

//Get Weapon
router.get('/rivalWeapon', function(req, res){
  GameModel.findOne({"_id": req.query.gameId},
  function (err, game) {
    if (err) {
      console.log(err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      if (req.query.playerId == game.player1Id) {
        console.log("Rival weapon damage: "+game.player2Weapon);
        res.json({'Status':"OK",'weapon':game.player2Weapon});
      } else {
        console.log("Rival weapon damage: "+game.player1Weapon);
        res.json({'Status':"OK",'weapon':game.player1Weapon});
      }
    }
  })
})

//=====POST=====

/*router.post('/player', function(req, res) {
  GameModel.findOneAndUpdate({"_id":req.query.gameID}, { $inc: {"players": 1 }
  }, function (err, game) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      console.log("Current players updated");
      res.json({'status': 'OK', "message": "Current players: "+ game.players});
    }
  })
})*/

router.post('/player', function(req, res) {
  GameModel.findOneAndUpdate({"_id":req.query.gameId}, { $inc: {"players": 1 }
  }, function(err, game) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      console.log("Current players updated");
      res.json({'status': 'OK', "message": "Current players: " + (game.players + 1)});
    }
  });
});

let players = 0;

router.post('/weapon', function(req, res) {
  players = players + 1;
  let playerId = "";
  let playerWeapon = "";
  if (players == 1) {
    console.log("1");
    playerId = "player1Id";
    playerWeapon = "player1Weapon";
  } else {
    console.log("2");
    playerId = "player2Id";
    playerWeapon = "player2Weapon";
  }

  GameModel.findOneAndUpdate({"_id":req.query.gameId}, { $set: {players: players,playerId: req.query.playerId, playerWeapon: req.query.weapon}}).then(
    function (game, err) {
      if (err) {
        console.log('Error: '+ err);
        res.json({'status': 'ERROR', "message": err});
      } else {
        console.log(game);
        console.log(err);
        console.log("Player weapon set updated");
        res.json({'status': 'OK', "message": "Player weapon set updated\n"});
      }
    })
})


app.use("/", router); 

//=====SERVER=====
app.listen(PORT, () => console.log(`Listening on https://localhost:${ PORT }`));