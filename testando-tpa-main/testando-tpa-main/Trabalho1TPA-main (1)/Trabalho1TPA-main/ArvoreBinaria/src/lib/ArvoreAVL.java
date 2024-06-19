package lib;

import java.util.Comparator;

public class ArvoreAVL<T> extends ArvoreBinaria<T> {

    public ArvoreAVL(Comparator<T> comparator) {
        super(comparator);
    }

    @Override
    public void adicionar(T novoValor) {
        raiz = adicionarRecursivamente(raiz, novoValor);
    }

    private No<T> adicionarRecursivamente(No<T> noAtual, T novoValor) {
        if (noAtual == null) {
            return new No<>(novoValor);
        }

        int comparacao = comparador.compare(novoValor, noAtual.getValor());

        if (comparacao < 0) {
            noAtual.setFilhoEsquerda(adicionarRecursivamente(noAtual.getFilhoEsquerda(), novoValor));
        } else if (comparacao > 0) {
            noAtual.setFilhoDireita(adicionarRecursivamente(noAtual.getFilhoDireita(), novoValor));
        } else {
            // Valor duplicado
            return balancear(noAtual);
        }

        noAtual.setAltura(1 + Math.max(altura(noAtual.getFilhoEsquerda()), altura(noAtual.getFilhoDireita())));
        return balancear(noAtual);
    }

    @Override
    public T remover(T valor) {
        raiz = removerRecursivo(raiz, valor);
        return valor;
    }

    private No<T> removerRecursivo(No<T> no, T valor) {
        if (no == null) {
            return no;
        }

        int comparacao = comparador.compare(valor, no.getValor());

        if (comparacao < 0) {
            no.setFilhoEsquerda(removerRecursivo(no.getFilhoEsquerda(), valor));
        } else if (comparacao > 0) {
            no.setFilhoDireita(removerRecursivo(no.getFilhoDireita(), valor));
        } else {
            if (no.getFilhoEsquerda() == null || no.getFilhoDireita() == null) {
                no = (no.getFilhoEsquerda() != null) ? no.getFilhoEsquerda() : no.getFilhoDireita();
            } else {
                No<T> temp = encontrarMinimo(no.getFilhoDireita());
                no.setValor(temp.getValor());
                no.setFilhoDireita(removerRecursivo(no.getFilhoDireita(), temp.getValor()));
            }
        }

        if (no == null) {
            return no;
        }

        no.setAltura(1 + Math.max(altura(no.getFilhoEsquerda()), altura(no.getFilhoDireita())));
        return balancear(no);
    }

    private No<T> encontrarMinimo(No<T> no) {
        while (no.getFilhoEsquerda() != null) {
            no = no.getFilhoEsquerda();
        }
        return no;
    }

    private int altura(No<T> no) {
        return (no == null) ? 0 : no.getAltura(); // Altura de um nó nulo é 0
    }

    private int fatorDeEquilibrio(No<T> no) {
        return (no == null) ? 0 : altura(no.getFilhoEsquerda()) - altura(no.getFilhoDireita());
    }

    private No<T> balancear(No<T> no) {
        int fator = fatorDeEquilibrio(no);

        if (fator > 1) {
            if (fatorDeEquilibrio(no.getFilhoEsquerda()) < 0) {
                no.setFilhoEsquerda(rotacaoEsquerda(no.getFilhoEsquerda()));
            }
            return rotacaoDireita(no);
        }
        if (fator < -1) {
            if (fatorDeEquilibrio(no.getFilhoDireita()) > 0) {
                no.setFilhoDireita(rotacaoDireita(no.getFilhoDireita()));
            }
            return rotacaoEsquerda(no);
        }

        return no;
    }

    private No<T> rotacaoDireita(No<T> y) {
        No<T> x = y.getFilhoEsquerda();
        No<T> T2 = x.getFilhoDireita();

        x.setFilhoDireita(y);
        y.setFilhoEsquerda(T2);

        y.setAltura(1 + Math.max(altura(y.getFilhoEsquerda()), altura(y.getFilhoDireita())));
        x.setAltura(1 + Math.max(altura(x.getFilhoEsquerda()), altura(x.getFilhoDireita())));

        return x;
    }

    private No<T> rotacaoEsquerda(No<T> x) {
        No<T> y = x.getFilhoDireita();
        No<T> T2 = y.getFilhoEsquerda();

        y.setFilhoEsquerda(x);
        x.setFilhoDireita(T2);

        x.setAltura(1 + Math.max(altura(x.getFilhoEsquerda()), altura(x.getFilhoDireita())));
        y.setAltura(1 + Math.max(altura(y.getFilhoEsquerda()), altura(y.getFilhoDireita())));

        return y;
    }
}
