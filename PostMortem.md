# Slutprojekt, Programmering 2
Rasmus Johansson 07-06-2023

## Inledning
Syftet med detta arbete var att använda det vi lärt oss hitills i kursen programmering 2 för att skapa ett projekt som använder sig av två av de ämnen vi täckt. Detta arbete använder sig av databasen och gui. 

## Bakgrund 
Arbetet började med att skapa en skiss och planering över arbetet. Bland annat skapade vi klass diagram för att ge en insikt i hur programmet skulle se ut. Vi skrev även loggbok över hur arbetet gick. 
Jag började med att implementera Random Walk algoritmen till en 2D text yta. Efter att jag fått detta att fungera såg gjorde jag så att den returnar kartan i formatet av en dubbel lista. Efter detta började jag jobba på spelaren och texturer. Jag använde mig av gratis sprites för detta. Jag testade länge runt spelarens 'movement'. Jag funderade på att använda mig av grid-baserade rörelser men valde eventuellt att fortsätta med en enkel fyra riktnings rörelser. Jag började även implementera grafikmotorn spelet skulle använda sig av. Denna motor var den jag använde förra året. Efter detta dock var jag fast ganska länge, jag var inte helt säker på hur jag skulle fortsätta utveckla spelet. Detta ledde tyvärr till att mycket tid till projektet försvan. Jag började eventuellt jobba mer på projektet. Jag fixade till spelarens rörelser samt såg till att spelaren kolliderade med väggar och andra hinder. Jag la till en funktion som skapade nya fiender efter ett visst antal frames passerat. Dessa fiender var tills vidare statiska och saknade funktioner. Jag la även till projektiler som spelaren kan kasta för att eventuellt göra skada på fiender. Efter detta började jag jobba på ett nytt kart system. Istället för att slumpmässigt skapa kartor kan spelaren på ett enkelt sätt skapa egna kartor genom att använda sig av 30x20px bilder där olika färger representerar olika texturer. Jag var ganska nöjd över denna idé och lyckades implementera den utan vidare trubbel. Enda problemet jag stötte på var att Java inte kunde se skillnad på svarta och genomskinliga pixlar. Då detta var klart började jag jobba på fiendens rörelser. Detta var inte lika svårt som jag förväntade mig då allt jag egentligen gjorde var att kolla ifall en fiendes x och y koordinater stämde med spelaresn. Ifall de inte gjorde det tog det ett steg i den riktning som krävdes. Spelaren förlorar ifall den blir rörs av en fiende. Något jag egentligen skulla gjord från början var att dela upp motorn så att den följde mvc principen. Eftersom att jag inte hade gjort det än delade jag upp programmet i en model, view och controller. Slutligen la jag till en enkel Game Over sekvens. Ifall spelaren dör kommer en ny ruta fram där de kan mata in sitt namn och sedan få en sorts arkad liknande lista över vilka som har flest poäng.

## Positiva Erfarenheter
Att läsa av bilderna och sedan omvandla dem till kartor var mycket enklare än vad jag förväntade mig. Det är ett väldigt smidigt sätt för både utvecklaren och spelaren att skapa innehåll. 
Utöver detta tycker jag även det är ganska enkelt att jobba med denna motor. 
Att hämta information från databasen var inte heller särskillt krångligt. 

## Negativa Erfarenheter
Ett stort problem för mig under detta projekt var att jag inte riktigt viste hur jag skulle göra mitt projekt. Detta ledde till att jag tappade mycket tid som jag kunde använt. Istället för att stressa ihop något hade jag kunnat utveckla på de idéer jag först hade. Jag bytte även ofta min idé gällande spelet. Detta ledde till att jag aldrig riktigt kom någon stans då jag började om flera gånger. 
Ett annat problem jag stötte på var att dela upp programmet i mvc. Däremot var detta på grund av att jag gjorde det efter att jag börjat jobba i programmet. Bästa scenario hade varit att jag delade upp projektet i mvc innan jag började jobba. 
Jag hade även vissa problem med att IntelliJ klagade på vissa bilder. Detta gick dock att fixa genom att bara starta om IntelliJ.

## Sammanfattning
Sammanfattningsvis tycker jag att det är kul att jobba på dessa projekt, jag skulle bara sett till att jag hade en bättre och mer konkret idé samt plan och att jag varit bättre på att hantera tiden. 
