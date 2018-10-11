IA multi-agent system for SoccerBot simulation platform made by Louis OUTIN and Marwan LAKRADI. (Winner of the 2017-2018 tournament of the DECIM Master). 

How to run it:

1) Download the Robocup SoccerBot simulator at http://www.cs.cmu.edu/~trb/TeamBots/index.html#INSTALLING

2) Put the folder "lakradiOutinTeam/" in "teams/" folder of SoccerBot.

3) Compile if necessary with:
javac lakradiOutinTeam/*.java lakradiOutinTeam/behaviour/*.java 

4) Change robocup.dsc config file with for each robot config line after:
	robot EDU.gatech.cc.is.abstractrobot.SocSmallSim
insert:
	lakradiOutinTeam.TeamLakradiOutin

4) Run ./demo

///

Intelligence de SoccerBot réalisé par Louis OUTIN et Marwan LAKRADI

1) Placer le dossier "lakradiOutinTeam/" dans le dossier "teams/" de SoccerBot.

2) Compilé si nécessaire avec la commande : 
javac lakradiOutinTeam/*.java lakradiOutinTeam/behaviour/*.java 

3) Configurer le fichier robocup.dsc avec les pour chaque ligne de chaque robot:
	Après : 
		robot EDU.gatech.cc.is.abstractrobot.SocSmallSim
	Placer : 
		lakradiOutinTeam.TeamLakradiOutin

4) Lancer ./demo

Remarque: la JavaDoc du projet est disponible dans lakradiOutinTeam/JavaDoc, et le rapport est dans le même dossier que ce README.
