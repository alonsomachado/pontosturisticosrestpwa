var stompClient = null;

const urlheroku = "https://turismopwa.herokuapp.com";

const urllocalhost = "http://localhost:8080";
//const urllocalhost = "https://turismopwa.herokuapp.com"; // Somente funciona com HTTPS

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#warningform").show();
    } else {
        $("#warningform").hide();
    }
    $("#greetings").html("");
}

function setWarningList(connected) {
    if (connected) {
        $("#warninglist").show();
    } else {
        $("#warninglist").hide();
    }
}

function connect() {
    var socket = new SockJS('/alonso-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/avisos', function (resposta) {
            console.log('subscribeEntity Avisos: ' + resposta);
            setWarningList(true);
            showAvisos(JSON.parse(resposta.body));
            alertaComum(JSON.parse(resposta.body));
        });
    });
}

function connectProb() {
    var socket = new SockJS('/alonso-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/avisos', function (resposta) {
            console.log('subscribeEntity Avisos: ' + resposta);
            setWarningList(true);
            showAvisos(JSON.parse(resposta.body));
            alertaComum(JSON.parse(resposta.body));
        });
        stompClient.subscribe('/topic/vote', function (resposta) {
              console.log('subscribeEntity Vote: ' + resposta);
              //setWarningList(true);
              //updateVote(JSON.parse(resposta.body));
              alertaVote(JSON.parse(resposta.body));
        });
        stompClient.subscribe('/topic/comentario', function (resposta) {
                      console.log('subscribeEntity Comment: ' + resposta);

                      showComments(JSON.parse(resposta.body));
                      //alertaComment(JSON.parse(resposta.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendAviso() {
    stompClient.send("/app/aviso", {}, JSON.stringify({'title': $("#title").val(), 'description': $("#description").val()}));
}

function sendVote(voto) {
    stompClient.send("/app/vote", {}, JSON.stringify(voto));
}

function sendComment(comment) {
    stompClient.send("/app/comentario", {}, JSON.stringify(comment));
}

function showAvisos(message) {
    //$("#listaArticles").append("<tr><td>" + JSON.stringify(message) + "</td></tr>");
    $("#listaAvisos").append("<tr><td>" + message.title + "</td> <td>" + message.description +"</td></tr> ");
 }

function showSpot(message) {
    $("#listaSpots").append("<div class='card'> <h5 class='card-header'> Titulo  </h5> <div class='card-body'> <h5 class='card-title'> Special title treatment </h5><p class='card-text'> TEXTO </p> <a href='#' class='btn btn-primary'>BOTAO</a> </div> </div>");
 }

 function showComments(comentdata) {
    console.log("COMENTARIO: VERIFICAR ID: "+document.getElementById("hid").value+" COM "+ comentdata.spotid);
    if(comentdata.spotid == document.getElementById("hid").value ){
        $("#comments").append("<div>"+comentdata.comentario+"</div>");
     }
  }
function alertaComum(message) {
  alert(message.title+" \n"+message.description);
 }

function alertaVote(message) {
  alert(message.tipo+" \n"+message.spotid);
 }

 function alertaComment(message) {
   alert(message.comentario+" \n"+message.spotid);
  }

 function login(){

  	//var url = "http://localhost:8080/login";
  	var url = urllocalhost+"/login";
  	console.log("Tentativa de Login na URL "+url);
  	var data = {};
  	data.username = document.getElementById("usernamep").value;
  	data.password = document.getElementById("senhap").value;

  	var jsondata = JSON.stringify(data);

  	var xhr = new XMLHttpRequest();
  	xhr.open("POST", url, true);
  	xhr.setRequestHeader('Content-type','application/json; charset=utf-8');
  	xhr.onload = function () {
  		if (xhr.readyState == 4 && xhr.status == "200") {
  			console.log("LOGIN ");
  			//sessionStorage.setItem('teste',"42"); //variavel de sessão só para aquela ABA
  			localStorage.setItem('usernamep',document.getElementById("usernamep").value) //variavel local para o site todo
  			localStorage.setItem('logado','true')
  			checklogado();
  		} else {
  			// Error
  		}
  	}
  	xhr.send(jsondata);
  	saveip();
  }

function registrar() {

    //var url = "http://localhost:8080/register";
	var url = urllocalhost+"/register";
	var data = {};
	data.username = document.getElementById("usernamep").value;
	data.password = document.getElementById("senhap").value;

	var jsondata = JSON.stringify(data);

	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	xhr.setRequestHeader('Content-type','application/json; charset=utf-8');
	xhr.onload = function () {
		if (xhr.readyState == 4 && xhr.status == "200") {
			console.log("REGISTRAR "+xhr.responseText);
			checklogado();
		} else {
			checklogado();
		}
	}
	xhr.send(jsondata);
}

function loginadmin(){

	//var url = "http://localhost:8080/loginadmin";
	var url = urllocalhost+"/loginadmin";
	console.log("Tentativa de Login ADMIN na URL "+url);
	saveip();
	console.log("IP Guardado e bloqueado em 3 tentativas ");
	var data = {};
	data.username = document.getElementById("usernameadmin").value;
	data.password = document.getElementById("senhaadmin").value;

	var jsondata = JSON.stringify(data);

	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	xhr.setRequestHeader('Content-type','application/json; charset=utf-8');
	xhr.onload = function () {
		if (xhr.readyState == 4 && xhr.status == "200") {
			console.log("LOGIN ADMIN ");
			localStorage.setItem('usernamep',document.getElementById("usernameadmin").value)
			localStorage.setItem('logado','true')
			sessionStorage.setItem('tokadm',"ADMINFAKE35435"); //variavel de sessão só para aquela ABA
			admlogado();
		} else {
			// Error
		}
	}
	xhr.send(jsondata);
}

function checklogado() {
	if(localStorage.getItem('logado') == 'true'){
		document.getElementById("main-content").removeAttribute('disabled');
		document.getElementById("main-content").removeAttribute('hidden');
		document.getElementById("menu").removeAttribute('hidden');
		document.getElementById("createuserforms").setAttribute('hidden','hidden');
	}else{
		document.getElementById("main-content").setAttribute('hidden','hidden');
		document.getElementById("menu").setAttribute('hidden','hidden');
		document.getElementById("createuserforms").removeAttribute('hidden');
	}
}
function admlogado() {
    if(sessionStorage.getItem('tokadm') == 'ADMINFAKE35435'){
        document.getElementById("adminloginforms").setAttribute('hidden','hidden');
        checklogado();
    }else{
        document.getElementById("adminloginforms").removeAttribute('hidden');
    }
}
function logout() {
	localStorage.clear();
	checklogado();
	location.replace(urllocalhost);
	//location.href = urllocalhost;
}
function saveip() {
    $.getJSON('https://api.ipify.org?format=jsonp&callback=?', function(data) {
                console.log(JSON.stringify(data, null, 2));
                sessionStorage.setItem('ip',data.ip);
    });
}

function getProblems(){

    //var url  = "http://localhost:8080/problems";
    var url  = urllocalhost+"/spots";
    var xhr  = new XMLHttpRequest();
    xhr.open('GET', url, true);

    // get autorization token from local storage
    //xhr.setRequestHeader('Authorization','Bearer ' + localStorage.getItem('token'));
    xhr.onload = function () {
        //console.log(xhr.responseText);

        //var notes = JSON.parse(JSON.stringify(xhr.responseText)); //Volta array inteiro de problemas
        var notes = xhr.responseText;
        console.log(notes);
        //var JSONArray problemarray = JSON.parse(JSON.stringify(xhr.responseText));
        //debugger;
        //var probitem = JSON.parse(notes);
        //console.log(probitem);
        probitem = JSON.parse(JSON.stringify(xhr.responseText));
        console.log(probitem);

        if (xhr.readyState == 4 && xhr.status == "200") {
            document.getElementById('listaSpots').innerHTML = "";
            if (notes == null || notes.length==0){
                document.getElementById('listaSpots').innerHTML = "Não Existem Pontos Cadastrados.."
            }

            showSpot(notes);

            //for ( int i=0 ; i< problemarray.length(); i++ ) {
             //          JSONObject item = problemarray.getJSONObject(i);
            //var item2 = JSON.parse(notes);
            //console.log(item2);
            //for(var i = 0; i < notes.length; i++){
            //    var item = notes[i]; //notes.getJSONObject("id");
            //    console.log(item);
            //     $("#listaSpots").append("<tr><td>" + item.title + "</td> <td>" + item.description +"</td><td> "+ "<a href='#' class='btn btn-primary btn-lg'>"+ item.id +"</a></td></tr> " );
            //}
            $("#resposta").append("<span>"+notes+"</span>");
        } else {
            alert('error in request');
        }
    }
    xhr.send(null);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});