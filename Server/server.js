var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

var begin = 0;
var roomnext=1;
var roomnumreal = 1;
var room;
var size_room=0;
var salas_disponiveis= [];
var clients_names= [];
var clients_rooms = [];
var clients_ids = [];

io.on("connection", function (client) {

  client.on("me",function(){
    client_id = client.id;
    client.emit("me", {"_id":client_id});
  });

  client.on("join_room", function(nome_client){ 
    if(salas_disponiveis.length > begin){
      console.log('array_antes: '+salas_disponiveis[begin]);
      roomnumreal = salas_disponiveis[begin]
      salas_disponiveis.splice(begin, 1);
      console.log('array_depois: '+ salas_disponiveis[begin]);
    }
    else{
      if(io.nsps['/'].adapter.rooms["room-"+roomnext] && io.nsps['/'].adapter.rooms["room-"+roomnext].length > 1){
        roomnext++;
      }
      console.log('proxima sala: '+roomnext);
      roomnumreal = roomnext;
    }
    console.log("id: " + nome_client + " sala atual: "+ roomnumreal);
    clients_rooms[client.id]= roomnumreal;
    clients_names[client.id] = nome_client;  
    clients_ids[client.id] = client.id;
    client_id= client.id;


    client.join("room-"+roomnumreal)
    room= Object.keys(io.nsps['/'].adapter.rooms["room-"+roomnumreal].sockets)
    size_room = room.length;

    if(size_room==1){
      io.in("room-"+roomnumreal).emit('join_room',  makeJson(roomnumreal, client_id, nome_client, "Welcome :), wait someone!"));
    }
    else{
      var peer_nome = clients_names[room[0]];
      var peer_id = clients_ids[room[0]];

      client.emit('join_room', makeJson(roomnumreal, client_id, nome_client,"Welcome :), wait someone!"));
      client.emit('join_room',  makeJson(roomnumreal, peer_id, peer_nome,"enter in this room!"));
      client.broadcast.to("room-"+roomnumreal).emit('join_room',  makeJson(roomnumreal, client_id, nome_client,"enter in this room!"));
    }
  });

  client.on("connect_room", function(nome, room_connect){
    clients_rooms[client.id]= room_connect;
    clients_names[client.id] = nome; 
    clients_ids[client.id] = client.id;

    client.join("room-"+room_connect);
    client.emit("connect_room", {"_id": client.id});
 
    // GET NO MONGO
    console.log(nome+ ' voltando pra sala '+room_connect); 
  });

  client.on("send_message", function(msg){
    var sala = clients_rooms[client.id];
    var nome_client = clients_names[client.id]
    var id_client = clients_ids[client.id]

    /*if(io.nsps['/'].adapter.rooms["room-"+sala].length == 1){
      io.in("room-"+sala).emit('send_message', makeJson(sala,nome_client,"wait someone!"));
    }*/
    io.in("room-"+sala).emit('send_message', makeJson(sala, id_client, nome_client,msg));

    // SALVAR NO MONGO!!!

   
    
  });

  client.on("leave_room", function(){
    var sala = clients_rooms[client.id];
    var nome_client= clients_names[client.id]
    var id_client = clients_ids[client.id] 
    if(typeof sala !== "undefined"){
      client.broadcast.to("room-"+sala).emit('leave_room', makeJson(sala,id_client,nome_client,"leave the room. Wait someone!"));
      client.leave("room-"+sala);
      salas_disponiveis.push(sala);
      // REMOVER DO MONGO
    }
    
    console.log(nome_client+ ' saiu da '+sala);
  });

  client.on("disconnect", function(){
    console.log("Disconnect " + clients_names[client.id] );
    delete clients_names[client.id];
    delete clients_rooms[client.id];
    delete clients_ids[client.id];
  });

});



http.listen(3000, function(){
  console.log('listening on *:3000');
});

function makeJson(room, _id, client_nome, mensagem){
  var data = getData();
  var obj = new Object();
  obj.room= room;
  obj._id = _id
  obj.client_nome = client_nome;
  obj.msg = mensagem;
  obj.dia = data.dia;
  obj.hora = data.hora;
  var jsonString= JSON.stringify(obj);

  return jsonString;
}

function getData(){
  var data =new Object();
  var date = new Date();
  data.hora = date.getHours() +":"+ date.getMinutes();
  data.dia = date.getUTCDate()+"/"+date.getMonth()+"/"+date.getFullYear();


  return data;
}
