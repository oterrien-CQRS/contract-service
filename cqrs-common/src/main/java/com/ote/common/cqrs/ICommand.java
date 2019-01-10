package com.ote.common.cqrs;

import com.ote.common.Validable;

public interface ICommand extends Validable {

    String getId();
}
