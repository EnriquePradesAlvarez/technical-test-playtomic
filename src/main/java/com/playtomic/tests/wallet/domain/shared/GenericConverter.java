package com.playtomic.tests.wallet.domain.shared;

public interface GenericConverter<D, E> {

    E mapAtoB(D obj2);

    D mapBtoA(E obj);
}
