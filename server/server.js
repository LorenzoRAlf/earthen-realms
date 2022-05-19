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
const games = require('./models/games');
const users = require('./models/users');
let GamesAvailable = [];

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

//GET Player weapons and armor
router.get('/userWeapons', function (req, res) {
  UserModel.findOne({"_id": req.query.playerId}, function (err, user) {
    if (err) {
      res.send(err);
    }
    res.json({"status":"OK","weapons":user.weaponList})
  })
})

//GET Player weapons and armor
router.get('/userArmors', function (req, res) {
  UserModel.findOne({"_id": req.query.playerId}, function (err, user) {
    if (err) {
      res.send(err);
    }
    res.json({"status":"OK","armors":user.armorList})
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
  UserModel.updateOne({session_token: req.query.session_token}, 
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
        res.json({'Status':"OK",'armor':game.player2Armor});
      } else {
        console.log("Rival armor sent");
        res.json({'Status':"OK",'armor':game.player1Armor});
      }
    }
  })
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
  for (let game in GamesAvailable) {
    if (GamesAvailable[game]["gameId"] == req.query.gameId) {
      if (req.query.playerId == GamesAvailable[game]["player1Id"]){
        res.json({"status":"OK","playerDamage":GamesAvailable[game]["player2Damage"]});
        console.log("Damage taken by player 1:"+GamesAvailable[game]["player2Damage"]);
      } else {
        res.json({"status":"OK","playerDamage":GamesAvailable[game]["player1Damage"]});
        console.log("Damage taken by player 2:"+GamesAvailable[game]["player1Damage"]);
      }
    }
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

//Get users and points
router.get('/usersAndPoints', function(req, res) {
  UserModel.find({}).select({"username": 1, "points":1, "_id": 0}).sort({"points":-1}).then(
    function (users, err) {
      if (err) {
        res.json({"status":"OK","message":err});
      } else {
        console.log(users);
        res.json({"status":"OK","userList":users});
      }
    })
})


//=====POST=====

router.post('/setPlayer', function(req, res) {
GameModel.findOneAndUpdate({"status":"waiting"}, {"players":2,
"status":"fighting",
"player2Id":req.query.playerId,
"player2Weapon":req.query.weapon,
"player2Armor":req.query.armor},{new: true}).then(
  function(data, err) {
    if (err) {
      console.log(err);
      res.json({"status": 'ERROR', "message":"tuki"});

    } else if (data) {

      var game = {"gameId":data._id,
      "player1Id":data.player1Id,
      "player2Id":data.player2Id,
      "player1Weapon":data.player1Weapon,
      "player2Weapon":data.player2Weapon,
      "player1Armor":data.player1Armor,
      "player2Armor":data.player2Armor,
      "player1Damage":-1,
      "player2Damage":-1}

      GamesAvailable.push(game);

      res.json({"status": 'OK', "game":data});

    } else {
      var game = new GameModel({"players":1,
        "status":"waiting",
        "player1Id":req.query.playerId,
        "player2Id":"",
        "player1Weapon":req.query.weapon,
        "player2Weapon":"",
        "player1Armor":req.query.armor,
        "player2Armor":"",
        "winner":""});
      game.save(function(err, game) {
        if (err) {
          res.json({"status": 'ERROR', "message":"Could not save new game"});
        } else {
          console.log(game);
          res.json({"status": 'OK', "game":game});
        }
      })
    }
  })
})

router.post('/finishGame', function(req, res) {
  UserModel.findOneAndUpdate({"_id": req.query.playerId},{$inc: {"coins": 10, "points":15}}, function(err) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      console.log("Player updated");
    }
  })
  
  GameModel.findOneAndUpdate({"_id": req.query.gameId}, {"status": "finished", "winner":req.query.playerId}, function(err) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      console.log("Game ended");
      res.json({'status': 'OK', "message":"Game finished."});
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

//Charge coins
router.post('/chargeCoins', function(req, res) {
  let coinsCharged = parseInt(req.query.coins);
  UserModel.findOneAndUpdate({"_id":req.query.playerId}, {$inc: {coins:(-1*coinsCharged)}},{new: true},
  function(err, player) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      console.log("Coins charged: " +  coinsCharged + ", Total coins: " + player.coins);
      res.json({'status': 'OK', "Coins charged": coinsCharged, "Total coins": player.coins});
    }
  })
})


//Set damage to send to rival player
router.post('/setDamage', function(req, res) {
  console.log("Searching for: "+req.query.gameId);
  for (let game in GamesAvailable) {
    console.log("Analyzing: "+GamesAvailable[game]["gameId"]);
    if (GamesAvailable[game]["gameId"] == req.query.gameId) {
      console.log("Game found");
      if (req.query.playerId == GamesAvailable[game]["player1Id"]){
        GamesAvailable[game]["player1Damage"] = req.query.playerDamage;
        console.log("Damage set by Player 1: "+GamesAvailable[game]["player1Damage"]);
        res.json({"status":"OK","message":"Damage updated"});
      } else {
        GamesAvailable[game]["player2Damage"] = req.query.playerDamage;
        console.log("Damage set by Player 2: "+GamesAvailable[game]["player2Damage"]);
        res.json({"status":"OK","message":"Damage updated"});
      }
    }
  }
})

//Reset damage recieved for next round
router.post('/resetDamage', function(req, res) {
  for (let game in GamesAvailable) {
    if (GamesAvailable[game]["gameId"] == req.query.gameId) {
      if (GamesAvailable[game]["player1Id"] == req.query.playerId){
        GamesAvailable[game]["player2Damage"] = -1;
      } else {
        GamesAvailable[game]["player1Damage"] = -1;
      }
    res.json({"status":"OK","message":"Damage reset"});
    }
  }
})

//Add weapon to player
router.post('/unlockWeapon', function(req, res) {
  UserModel.findOneAndUpdate({"_id":req.query.playerId}, {$push: {weaponList:req.query.weapon}},
  function(err, player) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      console.log("New weapon unlocked");
      res.json({'status': 'OK', "message": "New weapon unlocked successfully"});
    }
  })
})

//Add armor to player
router.post('/unlockArmor', function(req, res) {
  UserModel.findOneAndUpdate({"_id":req.query.playerId}, {$push: {armorList:req.query.armor}},
  function(err, player) {
    if (err) {
      console.log('Error: '+ err);
      res.json({'status': 'ERROR', "message": err});
    } else {
      console.log("New armor unlocked");
      res.json({'status': 'OK', "message": "New armor unlocked successfully"});
    }
  })
})

app.use("/", router); 

//=====SERVER=====
app.listen(PORT, () => console.log(`Listening on https://localhost:${ PORT }`));