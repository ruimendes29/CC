***Por cada Anon GW:
->Um map de sessions para envio por UDP recebidas por TCP, cada session contém o IP do GW dest
->Um map de sessions para envio por TCP recebidas por UDP

->Uma thread que lẽ packs UDP e escreve-os no map de sessions para envio TCP.
    também lê os confirms e remove esses packs do map de envio por UDP.
    também lê packs "STARTSESSION", e gera a sessão respetivo no map para envio TCP.
    (Later) também lê packs "DELETESESSION" 
->Uma thread que lê o map da session e reenvia os packs expirados.

***Para cada cliente o GW cria uma session para envio UDP e lança duas threads:
->Uma que lê do TCP e escreve na session nesse map.
->Uma que lê da base de dados e ordenadamente vai enviando os packs por TCP e 
remove-os da base de dados.

->Ao receber um pack STARTSESSION o GW faz a mesma coisa.

(Later)Se cada uma destas verificar que o socket fechou deve arranjar maneira de fechar a session
e dizer ao outro AnonGW para fechar tambem.