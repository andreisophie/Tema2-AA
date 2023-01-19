# Tema 2 AA - Octavian's Saga

Made by Andrei Mârunțiș

## Solutiile alese

1. Trial

Pentru acest task am facut reducerea problemei Set Cover la problema Vertex Cover, pe care mai apoi stiu sa o reduc la SAT. Pasii reducerii sunt urmatorii:

- Variabilele `yi,r` = submultimea i este a r-a din acoperirea multimii (universului) initial
- Clauzele care verifica sa acopar toate cele k submultimi din acoperire, respectiv sa folosesc o submultime numai o data/ sa aleg o singura submultime pentru a fi a r-a din acoperire, sunt identice ca la problema Vertex Cover
- Pentru a verifica acoperirea tuturor elementelor din univers, folosesc clauze asemanatoare cu clauzele din problema Vertex Cover care verifica ca orice muchie are cel putin un varf in acoperire; aici, "muchiile" sunt elementele din univers, nodurile sunt multimile, iar o "muchie" poate fi incidenta pe mai mult de 2 noduri sau pe un singur nod

Spre exemplu: Pentru universul `M = {1, 2, 3, 4}` cu submultimile `S1 = {1, 2}`, `S2 = {2, 3, 4}`, `S3 = {2, 3}`, clauzele descrise la ultimul punct vor verifica:

- pentru elementul 1: apartine multimii S1, verifica ca submultimea S1 face parte din acoperire
- pentru elementul 2: apartine multimilor S1, S2 si S3, deci va verifica ca cel putin una dintre ele face parte din acoperire
- etc.

Consider cardinalul universului `size`, numarul de usbmulțimi din care pot sa aleg `n` si numarul de submulțimi pe care le va conține acoperirea `k`. Asadar, am:

- `k` clauze de tipul `(y1,r ∨ y2,r ∨ ... ∨ yn,r)`, pentru fiecare `r, 1 ⩽ r ⩽ k`, care verifica sa aleg cate o submultime pentru fiecare `k` submultimi din acoperire
- `n * k * (k - 1) / 2` clauze de tipul `(¬yi,r ∨ ¬yi,s)` pentru `1 ⩽ i ⩽ n`, `1 ⩽ r, s ⩽ k` si `r ̸= s`, care verifica ca un nod se afla cel mult o data in acoperire
- `n * (n - 1) / 2 * k` clauze de tipul `(¬yi,r ∨ ¬yj,r)` pentru `1 ⩽ i`, `j ⩽ n`, `1 ⩽ r ⩽ k` si `i ̸= j`, care verifica ca fiecare submultime este aleasa pentru cel mult un index al acoperirii
- `size` clauze de tipul `(∨ yi,r)` pentru `i` indecsii multimilor care contin fiecare element si `1 ⩽ r ⩽ k`, care verifica ca acoperirea acopara fiecare element din univers.

Translatarea valorilor `yi,r` din problema SAT in valori numerice pe care oracolul stie sa le foloseasca am facut-o cu formula: `x = i * k + r + 1`. Astfel, pentru a extrage indicele multimilor care intra in acoperire din solutia oracolului, folosesc formula `i = (x - 1) / k`. Mentionez ca in cod aceste valori pot fi adunate/scazute cu 1, caci indicii `i` si `r` merg de la `0` la `n-1`, respectiv `k-1`.

2. Rise

Pentru a rezolva aceasta problema, am redus-o la o problema asemanatoare cu cea de la task-ul Trial. Pasii sunt urmatorii:

- Citesc mai intai cartile pe care Octavian le are si cele pe care le vrea, apoi le pastrez in setul de carti pe care le vrea doar pe acelea pe care nu le are (fac diferenta `wants - owned`)
- Asociez fiecarei carti pe care Octavian o vrea un index numeric
- Citesc pachetele de carti pe care Octavian le poate cumpara, dar ignor cartile pe care le are deja sau cele pe care nu le doreste
- In urma citirii am un set de date asemanator cu cel de la problema Trial, dar mai simplu: nu mi se mai da un numar de submultimi care sa intre in acoperire. Astfel, mi se reduce numarul de clauze:
  - `yi` = pachetul i este selectat (face parte din acoperire)
  - Am doar `size` clauze de tipul `(∨ yi,r)` pentru `i` indecsii pachetelor care contin fiecare carte (`size` = numarul de carti pe care Octavian le vrea si nu le are inca)
- Acum cand apelez oracolul, nu mai trebuie sa fac nicio translatare (intrucat am doar `n` variabile pentru cele `n` pachete)

3. Redemption

Pentru a rezolva aceasta problema am implementat un algoritm Greedy care urmeaza urmatorii pasi:

- Citesc toate cartile si pachetele de carti
- Dintre pachetele pe care nu le-am cumparat inca, il aleg pe cel care contine cele mai multe carti pe care Octavian nu le are inca'
- Repet pasul anterior pana cand Octavian a obitnut toate cartile pe care le vrea

Algoritmul este foarte simplu. Mai departe voi explica putin cum aleg pachetul cu cele mai multe carti pe care Octavian nu le are:

- Am un `ArrayList` in care stochez toate pachetele pe care il sortez dupa urmatorul criteriu:
  - Am creat un `Comparator` care face intersectia pachetului de cumparat cu multimea cartilor pe care Octavian le vrea si nu le are (folosind `Collection.retainAll`)
  - Comparatorul va compara cardinalele celor doua multimi obtinute si acesta va fi criteriul dupa care se sorteaza vectorul
- Dupa ce aleg pachetul de carti pe care Octavian il va cumpara, sterg cartile din acel pachet din multimea cartilor pe care Octavian le vrea.

## Feedback

Tema este draguta, insa mi s-a parut foarte frustrant sa o rezolv. Subiectul temei (implementarea unei reduceri a problemei `Set Cover` la `SAT CNF`) mi s-a parut unul extre de dificil, fiindca el este abordat doar superficial in cadrul laboratoarelor si cursului (se aminteste ca exista, insa nu prea se dau detalii legate de implementare). Chiar daca am avut ca exemplu reducerea `Vertex Cover` la `SAT CNF`, nu mi s-a parut suficient ajutor, intrucat era prea abstracta solutia (muchii de graf cu mai mult/mai putin de 2 varfuri) si nu m-as fi gandit la asta fara sa caut pe internet idei despre reducerea problemei `Set Cover` la `Vertex Cover`. Si pana la urma, daca tot am folosit o sursa externa ca sa imi clarific problema, nu ar fi fost mai bine sa am sursa oferita de facultate macar?

## Referinte

Sursele din care m-am documentat sunt:

1. https://jeremykun.com/2015/05/04/the-many-faces-of-set-cover/
2. https://courses.engr.illinois.edu/cs473/fa2015/w/lec/slides/02_notes.pdf
3. https://optimization.cbe.cornell.edu/index.php?title=Set_covering_problem

