var stompClient = null;

const urlheroku = "https://problemascidade.herokuapp.com";

//const urllocalhost = "http://localhost:8080";
const urllocalhost = "https://problemascidade.herokuapp.com"; // Somente funciona com HTTPS

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

function showAvisos(message) {
    //$("#listaArticles").append("<tr><td>" + JSON.stringify(message) + "</td></tr>");
    $("#listaAvisos").append("<tr><td>" + message.title + "</td> <td>" + message.description +"</td></tr> ");
}

function alertaComum(message) {
  alert(message.title+" \n"+message.description);
 }


function getProblems(){

    //var url  = "http://localhost:8080/problems";
    var url  = urllocalhost+"/problems";
    var xhr  = new XMLHttpRequest();
    xhr.open('GET', url, true);

    // get autorization token from local storage
    //xhr.setRequestHeader('Authorization','Bearer ' + localStorage.getItem('token'));
    xhr.onload = function () {
        //console.log(xhr.responseText);

        var notes = JSON.parse(JSON.stringify(xhr.responseText)); //Volta array inteiro de problemas
        //var notes = JSON.stringify(xhr.responseText);
        //var notes = JSON.parse(xhr.responseText);
        console.log(notes);
        //var JSONArray problemarray = JSON.parse(JSON.stringify(xhr.responseText));

        if (xhr.readyState == 4 && xhr.status == "200") {
            document.getElementById('listaProblems').innerHTML = "";
            if (notes == null || notes.length==0){
                document.getElementById('listaProblems').innerHTML = "No notes.."
            }
            //for ( int i=0 ; i< problemarray.length(); i++ ) {
             //          JSONObject item = problemarray.getJSONObject(i);
            //var item2 = JSON.parse(notes);
            //console.log(item2);
            //for(var i = 0; i < notes.length; i++){
            //    var item = notes[i]; //notes.getJSONObject("id");
            //    console.log(item);
            //     $("#listaProblems").append("<tr><td>" + item.title + "</td> <td>" + item.description +"</td><td> "+ "<a href='#' class='btn btn-primary btn-lg'>"+ item.id +"</a></td></tr> " );

            //}
        } else {
            alert('error in request');
        }
    }
    xhr.send(null);
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
}

function loginadmin(){

	//var url = "http://localhost:8080/loginadmin";
	var url = urllocalhost+"/loginadmin";
	console.log("Tentativa de Login ADMIN na URL "+url);
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

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});