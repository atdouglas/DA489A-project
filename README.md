# Plantopedia
This repository is a school project made by students in Group 2 from Malmö Universitet during the course Systems Development II.

## Description
Plantopedia is a website with the purpose to help a user take care of plants. It also serves as a “plant wikipedia” providing information to the user in a coherent and uniform way. The website also provides more in depth information about plants including detailed descriptions and care guides for those who want more information about owned plants.


## Run instructions
In order to run the website, the server needs to be running first.

### Starting the server

> [!NOTE]
> During development the server has been running in IntelliJ. It is therefore recommended to use IntelliJ when running the server and the guide assumes that. If you are using something else to run the server then the instructions may not apply directly to you.

> [!NOTE]
> The server needs a .env file that should be placed at the top of the server folder. If you are a teacher, this .env file will be available in the zip file in the turn in.

1. Make sure you have [JDK version 23](https://www.oracle.com/se/java/technologies/downloads/#java23) installed.
2. Open the **server** folder as a project in IntelliJ.
3. Navigate to **Main.java** (server/src/main/java/se/myhappyplants/server/Main.java).
4. Run the Main.java file.

>[!IMPORTANT]
>If the server crashes, then the likely cause is that you did not place the .env file at the correct place or the .env file has the wrong name. Make sure the .env file is named **.env** (don't forget the dot!).

### Starting the website.

1. Make sure you have [npm and Node.js](https://nodejs.org/en/download) installed (on at least version 22.13.1)
2. Open a terminal in the **website** folder.
3. Run `npm install` to install all dependencies.
4. Run `npm run dev` to start the website.
5. Navigate to http://localhost:5173/ in your browser of choice and the website should be ready to use from there.

>[!IMPORTANT]
>There have been some problems when running the website in Safari. If you use Safari and encounter any problems, try using another browser.
