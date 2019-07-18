/**
 * Copyright 2018 The original authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
**/

package io.dekorate.component.decorator;

import io.dekorate.component.model.ComponentBuilder;
import io.dekorate.component.model.ComponentSpecBuilder;
import io.dekorate.component.model.DeploymentMode;
import io.dekorate.kubernetes.decorator.Decorator;

import java.util.Map;

public class MetadataDecorator extends Decorator<ComponentBuilder> {

  private final Map<String, String> labels;
  private final String name;

  public MetadataDecorator(String name, Map<String, String> labels) {
    this.name = name;
    this.labels = labels;
  }

  @Override
  public void visit(ComponentBuilder componentBuilder) {
    componentBuilder
      .withNewMetadata()
      .withName(name)
      .withLabels(labels)
      .endMetadata();
  }

}
