package com.ote.common.cqrs;

import com.ote.common.Validable;

public interface IEvent extends Validable {

    String getId();
}
