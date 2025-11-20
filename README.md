# PonteEstreita

Este projeto implementa o problema clássico da **Ponte Estreita**, onde:

* A ponte suporta **no máximo 3 carros ao mesmo tempo**
* A ponte é **mão única** (carros só podem atravessar em um único sentido por vez)
* Deve existir **justiça (fairness)** entre os sentidos Norte→Sul e Sul→Norte

O objetivo é garantir **segurança**, **ordem** e **ausência de condições de corrida**, usando diferentes técnicas de sincronização.

---

## **Estrutura do Projeto**

```
/semaforo
    Ponte.java      → solução utilizando Semáforos
    Carro.java
    Sentido.java

/monitor
    Ponte.java      → solução utilizando synchronized/wait/notifyAll
    Carro.java
    Sentido.java

Simulador.java      → executa a simulação com criação aleatória de carros
```

---

## **Funcionalidades Essenciais**

As duas implementações respeitam as mesmas regras:

### Capacidade Máxima

A ponte permite até **3 carros simultâneos**.

### Sentido Único

Enquanto houver carros atravessando, todos devem estar no **mesmo sentido**.

### Justiça (Fairness)

Quando a ponte fica vazia, o sistema permite que carros do outro lado possam entrar, evitando **inanição**.

---

## **Soluções Implementadas**

### **1. Monitor (synchronized + wait + notifyAll) — `/monitor/Ponte.java`**

* Usa `synchronized` para exclusão mútua
* Carros esperam via `wait()`
* Quando a ponte é liberada, usa `notifyAll()`
* Lógica simples e segura
* Ideal para entender o conceito de monitores em Java

### **2. Semáforo (Semaphore) — `/semaforo/Ponte.java`**

* Utiliza `Semaphore` como monitor manual
* Possui filas explícitas para cada sentido
* Permite controle mais detalhado
* Implementação mais próxima de sistemas de baixo nível
* Perfeita para estudar fairness mais fina

---

## **Simulador**

O arquivo `Simulador.java` cria um número configurado de carros (padrão: 20), cada um com sentido aleatório.

Uso:

```bash
java Simulador
```

Ele exibirá:

* entradas na ponte
* saídas
* trocas de sentido
* quantidade de carros esperando
* situação geral do sistema

---

## **O que observar durante a execução**

* Nunca deve haver **mais de 3 carros** ao mesmo tempo
* Nunca devem existir carros em **dois sentidos diferentes** simultaneamente
* Quando a ponte esvazia, o sentido pode inverter
* Em nenhum momento deve ocorrer **deadlock**
* Todos os carros eventualmente atravessam (sem starvation)
