# Eksamen PGR310 - DevOps i skyen ❤️

## Del 1

Her har selskapet en god del utfordringer. Det første vi kan ta tak i er at de har en manuell deployment-prosess hvor 
ops-teamet får et artifakt i form av en jar-fil som skal deployes til AWS. Man kan anta at utviklerne har testet jar-filen 
hos seg før de sender den, men det garanterer ikke at den kommer til å virke i produksjon (f.eks. hvis de har forskjellig 
java-versjon). Jeg tolker også case-beskrivelsen dit hen at de har en manuell installasjonsprosess i AWS. Dette gjør dem 
veldig utsatt for menneskelige feil ved at man enten gjør feil på ett av trinnene, eller totalt hopper over det ved uhell. 
Fra et DevOps-perspektiv vil de her bryte med prinsippet om **flyt**. Manuelle prosesser er stort sett å anse som “waste”, 
spesielt der det vil kreve relativt lite ressurser å sette opp automatisering. Det fører også til at hele utviklingsprosessen 
vil gå tregere, ettersom det vil ta lenger tid fra kode er skrevet til det havner i produksjon. Her vil det være mye å 
hente på å få på plass infrastruktur som kode (i.e. terraform), slik at AWS-miljøet blir konfigurert automatisk. I tillegg 
vil det være nødvendig å få på plass automatisk bygging av jar-filen, samt kanskje ta et steg videre til å bygge et docker-image 
i stedet. Da har de effektivt sett satt opp en full CD-pipeline.

Det kommer ikke helt klart frem fra oppgaven hvorvidt de har gode rutiner for automatisering av tester, men ettersom de 
stort sett ikke oppdager feil før de forsøker å deploye er det nærliggende å tro at dette ikke er tilfelle. Absolutt 
worst-case er hvis de ansetter testere som kun gjør manuelle tester. Da kan det ta veldig lang tid fra en bug blir introdusert 
i kode til den blir fanget opp av QA. Her har selskapet problemer med å få god **feedback** i tide, noe som kan løses med bedre 
testpraksis (i.e. ved å skrive bedre enhetstester og integrasjonstester). Jo mer de kan automatisere av testing, desto bedre 
flyt vil de klare å oppnå. Det vil også være en forutsetning for at de skal kunne deploye oftere (kommer tilbake til dette).

Når vi først er inne på testing kan vi også se på hva som kan være et problem ved å bolstre QA-avdelingen som svar på 
dårlig kodekvalitet. Tanken virker i og for seg god - å bruke mer ressurser på kvalitetssikring må jo føre til et bedre 
og mer stabilt produkt, og det er en god tanke å få et ekstra par øyne som kan se feil i koden man selv skriver. Problemet 
er at dette blir en separat prosses fra utviklingen. Igjen bryter vi med prinsippet om færrest mulige overleveringer. Og 
selv hvis testerne skriver integrasjonstester og enhetstester vil det fremdeles ikke være godt nok, ettersom utviklerne 
egentlig ikke har noe forhold til hvor robust koden deres burde være. Jeg tror det vil være langt mer å hente på å f.eks. 
innføre parprogrammering som metodikk. Da får man fordelen av et ekstra par øyne som kan heve kodekvalitet, kombinert med 
at det blir gjort samtidig som koden blir skrevet.

Et annet stort problem de har er at deployment kun skjer fire ganger i året. Dette er en forståelig reaksjon på en skjør 
kodebase, men som oppgaven beskriver er konsekvensen at man får levert nye features langt sjeldnere (noe som blir forsterket 
ved at ting ofte går galt). Selv om det kan virke som et lurt valg for å minimere risiko vil det mest sannsynlig føre til 
at selskapet risikerer mer. Det kan godt være at features de har brukt 3 måneder på å utvikle får kjempedårlig tilbakemeldinger, 
eller viser seg å være unødvendig. I praksis ender man opp med å batche arbeid, noe vi prøver å unngå i et DevOps-miljø 
(ønsker heller å få til single piece flow) Dersom de hadde deployet oftere ville det vært mulig å få tilbakemelding raskere 
og endre kurs ved behov. Man kan i tillegg gjøre gradvis utrulling av nye features med f.eks. feature-toggles, som lar 
en dynamisk skru på funksjonalitet for et subsett av brukere (eller alle) og se an hvilken effekt de har. Som nenvt 
tidligere forutsetter dette selvfølgelig at man har gode testrutiner i CI/CD-pipelinen sin, i tillegg til at man trenger 
metrikker fra den deployede koden sin slik at man kan tydeliggjøre hvordan kodeendringer påvirker kodebasen (en slags 
kontinuerlig puls/helsetest for systemet).

Den siste problemstillingen som må takles er at de har en separasjon mellom utvikling og ops. Logikken bak dette er 
kanskje at man kan sette folk som er flinke på vedlikehold til ops, og folk som er kreative og nyskapende på utvikling 
av nye features. Realiteten er nok heller at man får inn konsulenter som er betalt for å levere nye features fortest 
mulig (og til lavest pris), som så går videre på et nytt prosjekt etter et halvår. I disse situasjonene har ikke 
konsulentene egentlig noe insentiv på å levere god kodekvalitet. Det er tross alt ikke de som skal sørge for at systemet 
fortsetter å fungere senere. Som nevnt på forelesningene - de har ikke “skin in the game”. Denne holdningen er også 
gjenspeilet i at selskapet allokerer mer ressurser til QA i håp om å fikse problemene sine (og at de velger å leie inn 
spesialister på DevOps for å magisk skulle rydde opp i ting)- det hjelper lite om utviklerne ikke selv må tåle konsekvensene 
av svak kode. Det koster mer å investere i et skift av metode, men man får mye mer igjen for å slå sammen utviklerne og 
ops til én enhet, samt å “desentralisere” devops-kompetansen (i.e. at alle får et forhold til devops og må ta stilling 
til prosessene). Den siste puslespillbrikken som i så fall bør komme på plass (hvis den ikke allerede er det) er at man 
må ha gode rutiner for hva man gjør når (*ikke hvis*) noe går galt. Da er det essensielt at man ikke kjører pekeleken for 
å fordele skyld, men at man heller ser på det som en læringsmulighet og en mulighet til forbedring av prosesser. Kanskje 
kan man innføre en ny test-suite som fanger opp feilen man gjorde i senere bygg, eller kanskje man finner ut at noen ved 
uhell gjorde en kodeendring i en fil som burde ha vært urørt. Uansett er det som sagt da viktig at man har fokus på å lære 
av det, og ikke som noe man burde unngå. Hvis man da har gode devops-rutiner minimerer man også hvor stor risiko man tar 
for hver deployment - det er tross alt lettere å rulle tilbake en ettermiddag med arbeid enn tre måneder.

## Del 2 - CI

**Oppgave 3** 

Oppsett av branch protection:

1. Gå til Settings>Branches
2. Velg "Add branch protection rule".
3. Skriv inn "main" under branch name pattern.
4. Huk av for "Require a pull request before merging" for at ingen skal kunne pushe direkte
5. Huk av for "Require approvals" (mest sannsynlig automatisk huket av) som dukker opp etter trinnet over
6. Huk av for "Require status checks to pass before merging", og legg til "build" som status check for at CI og Docker må verifiseres først
7. Huk av for "do not allow bypassing the above settings" for at reglene også skal gjelde for eieren av repoet (og andre som har rolle med "bypass branch protections" satt)

## Del 3 - Docker

**Oppgave 1**

For å få denne til å funke med egen Dockerhub-konto forutsetter det at man legger til `DOCKER_HUB_USERNAME` og `DOCKER_HUB_TOKEN` 
under Settings>Secrets>Actions i repoet. Token må også først genereres gjennom docker sine nettsider. Disse vil ikke følge 
med når man lager en kopi av prosjektet (hvis ikke hadde det vært fryktelig enkelt å stjele hemmeligheter fra en bedrift
og potensielt gjøre en god del skade) 

**Oppgave 3**

For at ny docker.yml skal fungere mot AWS må følgende gjøres:

1. Gå til IAM>Users på AWS, og søk opp sin IAM-bruker
2. Under "Security credentials" må man så velge "Create access key", og så ta vare på de to tekststrengene man får (enten
laste ned .csv-fil, eller lagre det et annet sted)
3. Nøklene fra foregående steg må så legges inn som secrets, henholdsvis AWS_ACCESS_KEY_ID og AWS_SECRET_ACCESS_KEY
4. Siste steg er å endre ECR_REPOSITORY på linje 28 i `docker.yml` til sensor sitt repository (merk at imaget da ikke
lenger inkluderer shopifly i navnet sitt...)

## Del 5 - Terraform og CloudWatch Dashboards

**Oppgave 1**

Her er problemet at de ikke har satt opp en separat backend for terraform (en slags database for terraform-operasjoner). 
Da vil terraform opprette en lokalt på maskinen, og ettersom GitHub actions setter opp nye miljøer for hver run vil det 
bety at terraform også setter opp denne backenden på nytt hver gang. Med andre ord vil den ikke ha noen måte å se 
tidligere historikk over hvilke operasjoner som har blitt gjort via terraform, og vil tro at alt må settes opp fra bunnen av.
NB! Jeg endret også på workflowen slik at bucketen for analytics nå ender opp med å hete analytics-1054. Alternativt ville 
det letteste være å slette bucketen, og så la terraform lage den på nytt (men ettersom andre elever også har tilgang til 
bucketen er nok dette ikke å foretrekke på eksamen ;) )