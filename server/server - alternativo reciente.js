//==Dependencias
const express = require('express');
const app = express();
const router = express.Router();
const PORT = process.env.PORT;
//const PORT = 5000;
const mongoose = require('mongoose');
var cors = require('cors');
//Importamos los modelos
const UserModel = require('./models/users');
const WeaponModel = require('./models/weapons');
const ArmorModel = require('./models/armors');
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

//Player and game variables
let players = 0;

let player1Damage = -1;
let player2Damage = -1;

let currentPlayer1Id = "";
let currentPlayer2Id = "";

let player1HP = 100;
let player2HP = 100;

let player1Weapon;
let player1Armor;
let player2Weapon;
let player2Armor;

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

//GET armors()
router.get('/armors', function (req, res) {
  ArmorModel.find(function (err, armors) {
    if (err) {
      res.send(err);
    }
    res.json({"status":"OK","armor_list":armors})
  });
});

//GET items()
router.get('/items', function (req, res) {
  ItemModel.find(function (err, items) {
    if (err) {
      res.send(err);
    }
    res.json({"status":"OK","item_list":items})
  })
})


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

//Get Players
router.get('/players', async function(req, res){
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

//Get Armor
router.get('/rivalArmor', function(req, res){
  GameModel.findOne({"_id": req.query.gameId},
  function (err, game) {
    if (err) {
      console.log(err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      if (req.query.playerId == game.player1Id) {
        console.log("Rival armor sent");
        res.json({'Status':"OK",'armor':player2Armor});
      } else {
        console.log("Rival armor sent");
        res.json({'Status':"OK",'armor':game.player1Armor});
      }
    }
  })
})

router.get('/unstartedGame', function(req, res) {
  GameModel.findOne({"status": "waiting"},
  function(err, game) {
    if (game) {
      console.log(game);
      console.log("Game found");
      res.json({'status': 'OK', 'gameId' : game._id});
    } else {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    }
  });
})

router.get('/coins', function(req, res) {
  UserModel.findOne({"_id":req.query.playerId},
  function(err, player) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      console.log("Coins: " + player.coins);
      res.json({'status': 'OK', "coins": player.coins});
    }
  })
})


router.get('/damageTaken', function(req, res) {
  if (req.query.playerId == currentPlayer1Id){
    res.json({"status":"OK","playerDamage":player2Damage});
    console.log("Damage taken by player 1:"+player2Damage);
  } else {
    res.json({"status":"OK","playerDamage":player1Damage});
    console.log("Damage taken by player 2:"+player1Damage);
  }
})


router.get('/getGame', function(req, res) {
  GameModel.find({"_id": req.query.gameId}).then(
    function (game, err) {
      if (err) {
        res.json({"status":"OK","message":err});
      } else {
        res.json({"status":"OK","game":game});
      }
    })
})

//=====POST=====

router.post('/setPlayer', function(req, res) {
  GameModel.find({"_id": req.query.gameId}).then(
    function (data, err) {
      if (err) {
        console.log(err);
        res.json({'status': 'ERROR', "message": err});
      } else {
        if (data[0] == null) {
          console.log("Database request failed. Game not found");
          res.json({'status': 'ERROR', "message":"Database request failed. Game not found"});
        } else {
          //
          console.log(data[0]);
          if (data[0].players != null) {
            //Determine player number designation
            if (data[0].players == 0) {
              console.log("1");
              players = 1;
            } else {
              console.log("2");
              players = 2;
            } 
          } else {
            res.json(data[0]);
          }
        }
      }
    }
  )
  if (players == 1) {
    GameModel.findOneAndUpdate({"_id":req.query.gameId}, { $set: {status: "fighting",players : 2, player2Id: req.query.playerId, player2Weapon: req.query.weapon, player2Armor: req.query.armor}}).then(
      function (setGame, err) {
        if (err) {
          console.log('Error: '+ err);
          res.json({'status': 'ERROR', "message": err});
        } else {
          currentPlayer2Id = req.query.playerId;
          player2Weapon = req.query.weapon
          player2Armor = req.query.armor
          console.log("Player weapon set updated");
          res.json({'status': 'OK', "message": "Player weapon set updated\n"});
          players = 0;
        }
      })
  } else { 
    GameModel.findOneAndUpdate({"_id":req.query.gameId}, { $set: {players : 1, player1Id: req.query.playerId, player1Weapon: req.query.weapon, player1Armor: req.query.armor}}).then(
      function (setGame, err) {
        if (err) {
          console.log('Error: '+ err);
          res.json({'status': 'ERROR', "message": err});
        } else {
          currentPlayer1Id = req.query.playerId;
          player1Weapon = req.query.weapon
          player1Armor = req.query.armor
          console.log("Player weapon set updated");
          res.json({'status': 'OK', "message": "Player weapon set updated\n"});
        }
      })
  }
})

router.post('/endGame', function(req, res) {
  GameModel.findOneAndUpdate({"_id": req.query.gameId}, {"status": "waiting", "players":0,"player1Weapon":"","player2Weapon":"","player1Armor":"","player2Armor":"",
"player1Id":"","player2Id":""}, function(err) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      players = 0;
      currentPlayer1Id = "";
      currentPlayer2Id = "";
      console.log("Game ended");
      res.json({'status': 'OK', "message":"Game ended. Set as waiting. Player values reset"});
    }
  })
})

router.post('/addCoins', function(req, res) {
  let coinsAdded = parseInt(req.query.coins);
  UserModel.findOneAndUpdate({"_id":req.query.playerId}, {$inc: {coins:coinsAdded}},
  function(err, player) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      let totalCoins = coinsAdded + player.coins;
      console.log("Coins added: " +  coinsAdded + ", Total coins: " + totalCoins);
      res.json({'status': 'OK', "Coins added": coinsAdded, "Total coins": totalCoins});
    }
  })
})

//Set damage to send to rival player
router.post('/setDamage', function(req, res) {
  if (req.query.playerId == currentPlayer1Id){
    player1Damage = req.query.playerDamage;
    console.log("Damage set by Player 1: "+player1Damage);
  } else {
    player2Damage = req.query.playerDamage;
    console.log("Damage set by Player 2: "+player2Damage);
  }
  res.json({"status":"OK","message":"Damage updated"});
})
//Reset damage recieved for next round
router.post('/resetDamage', function(req, res) {
  if (currentPlayer1Id == req.query.playerId){
    player2Damage = -1;
  } else {
    player1Damage = -1;
  }
  res.json({"status":"OK","message":"Damage reset"});
})

app.use("/", router); 

//=====SERVER=====
app.listen(PORT, () => console.log(`Listening on https://localhost:${ PORT }`));