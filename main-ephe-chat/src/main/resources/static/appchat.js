let stompClient = null;
let myUserName = null;

const chatLineElementId = "body";
const usernameId = "usernameId";
const messageElementId = "messageInput";


const setConnected = (connected) => {
    const header = document.getElementById("header");
    const body = document.getElementById('body');
    const footer = document.getElementById('footer');
    const login = document.getElementById('login');
    const usersPanel = document.getElementById('users-panel');
    header.hidden = !connected;
    body.hidden = !connected;
    footer.hidden = !connected;
    login.hidden = connected;
    usersPanel.appendChild(meAsChatUser(myUserName));
    const fileControl = document.getElementById("fileupload");
    fileControl.addEventListener("change", () => {
        const endpoint = "/file";
        const formData = new FormData();
        formData.append("file", fileControl.files[0]);
        fetch(endpoint, {
            method: "POST",
            mode: "cors",
            body: formData,
        }).then(
            response => {
                return response.text();
            }).then(
            filename => {
                console.log("File sent: ", filename);
                sendFileMessage(filename);
            })
    });
    const messageInputField = document.getElementById("messageInput");
    messageInputField.addEventListener("keyup", (event) => {
       if (event.key === "Enter" || event.code === "Enter") {
           sendMsg();
       }
    });
}

const connect = () => {
    const username = document.getElementById(usernameId).value;
    myUserName = username;
    stompClient = Stomp.over(new SockJS('/websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log(`Connected as username: ${username} frame:${frame}`);
        stompClient.subscribe('/topic/response', (message) => showMessage(message), {usr: username});
        stompClient.subscribe('/topic/file', (message) => showMessage(message), {usr: username});
        stompClient.subscribe("/topic/response/" + username, (message) => showMessage(message), {usr: username});
    });
}

const sendMsg = () => {
    const username = document.getElementById(usernameId).value;
    const messageElem = document.getElementById(messageElementId);
    let message = messageElem.value;
    stompClient.send("/app/message", {}, JSON.stringify(
        {
            'userName': username,
            'messageStr': message
        }
    ))
    messageElem.value = '';
    messageElem.focus();
}

const showMessage = (message) => {
    const chatLine = document.getElementById(chatLineElementId);
    let messageText = JSON.parse(message.body).messageStr;
    let messageUser = JSON.parse(message.body).userName;
    if (messageUser === myUserName) {
        chatLine.appendChild(createMyAnswer(messageText, messageUser));
    } else {
        chatLine.appendChild(createOtherAnswer(messageText, messageUser));
    }
    chatLine.scrollTo(0, chatLine.scrollHeight);
}

//HTML factory methods

const createMyAnswer = (messageStr, userName) => {
    const nameLetter = userName.charAt(0).toUpperCase();
    let html = "<div class=\"d-flex align-items-start mb-5 my-reply\">\n" +
        "    <div class=\"position-relative avatar\">\n" +
        "        <span class=\"position-absolute top-50 start-50 translate-middle\">" + nameLetter + "</span>\n" +
        "    </div>\n" +
        "    <div class=\"pe-2\">\n" +
        "        <div>\n" +
        "            <div class=\"card card-text d-inline-block p-2 px-3 m-1\">" + messageStr + "\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>";
    return makeNodeFromTemplate(html);
}

const createOtherAnswer = (messageStr, userName) => {
    const nameLetter = userName.charAt(0).toUpperCase();
    let html = "<div class=\"d-flex align-items-start text-end justify-content-end mb-5\">\n" +
        "    <div class=\"pe-2\">\n" +
        "        <div>\n" +
        "            <div class=\"card card-text d-inline-block p-2 px-3 m-1\">" +
        messageStr +
        "</div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "    <div class=\"position-relative avatar\">\n" +
        "        <span class=\"position-absolute top-50 start-50 translate-middle m-0 p-0\">" +
        nameLetter +
        "</span>\n" +
        "    </div>\n" +
        "</div>";

    return makeNodeFromTemplate(html);
}

const meAsChatUser = (myUserName) => {
    const nameLetter = myUserName.charAt(0).toUpperCase();
    let html = "<ul class=\"navbar-nav me-auto align-items-center\">\n" +
        "                    <li class=\"nav-item\">\n" +
        "                        <div class=\"position-relative avatar\">\n" +
        "                            <span class=\"position-absolute top-50 start-50 translate-middle m-0 p-0\">" +
        nameLetter +
        "                             </span>\n" +
        "                        </div>\n" +
        "                    </li>\n" +
        "                    <li class=\"nav-item\">\n" +
        "                        <span class=\"h6 text-secondary ms-2\">" +
        myUserName +
        "</span>\n" +
        "                    </li>\n" +
        "                </ul>";

    return makeNodeFromTemplate(html);
}

const makeNodeFromTemplate = (rawHtml) => {
    const template = document.createElement("template");
    template.innerHTML = rawHtml.trim();
    const nNodes = template.content.childNodes.length;
    if (nNodes !== 1) {
        throw new Error("error creating answer for " + rawHtml.substring(0, 60) + "...");
    }
    return template.content.firstChild;
}

const sendFile = () => {
    const fileControl = document.getElementById("fileupload");
    fileControl.click();
}

const sendFileMessage = (filename) => {
    const username = document.getElementById(usernameId).value;
    const encodedFilename = encodeURI(filename);
    const messageText = `<a href="/file/${encodedFilename}">${filename}</a>`;
    stompClient.send("/app/file", {}, JSON.stringify(
        {
            'userName': username,
            'messageStr': messageText
        }
    ))
}
