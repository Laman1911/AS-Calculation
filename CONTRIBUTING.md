Projektstruktur
Projektet følger en lagdelt arkitektur:
controller – håndterer HTTP-requests
service – indeholder forretningslogik
repository – håndterer databaseadgang
model – domæneobjekter (Projekt, Delprojekt, Opgave)


Arbejdsgang og branches
Projektet anvender en simpel branch-strategi:
main indeholder den stabile version af applikationen
Nye ændringer udvikles lokalt og merges til main, når funktionaliteten virke