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

package io.dekorate.kind.loadservice;

import java.util.Collection;

import io.dekorate.BuildServiceApplicablility;
import io.dekorate.LoadService;
import io.dekorate.LoadServiceFactory;
import io.dekorate.config.ConfigurationSupplier;
import io.dekorate.kind.config.KindLoadConfig;
import io.dekorate.kubernetes.config.ImageConfiguration;
import io.dekorate.project.Project;
import io.fabric8.kubernetes.api.model.HasMetadata;

public class KindLoadServiceFactory implements LoadServiceFactory {

  private static final String KIND = "kind";
  private static final String MESSAGE_OK = "Kind load service is applicable.";
  private static final String MESSAGE_NOK = "Kind load service is not applicable to the project";

  @Override
  public LoadService create(Project project, ImageConfiguration config) {
    return new KindLoadService(project, config);
  }

  @Override
  public LoadService create(Project project, ImageConfiguration config, Collection<HasMetadata> resources) {
    return new KindLoadService(project, config);
  }

  @Override
  public int order() {
    return 30;
  }

  @Override
  public String name() {
    return KIND;
  }

  @Override
  public BuildServiceApplicablility checkApplicablility(Project project, ImageConfiguration config) {
    if (!(config instanceof KindLoadConfig)) {
      return new BuildServiceApplicablility(false, "Kind load config not found");
    }

    KindLoadConfig kindLoadConfig = (KindLoadConfig) config;
    boolean applicable = kindLoadConfig.isEnabled();
    String message = applicable
        ? MESSAGE_OK
        : MESSAGE_NOK;
    return new BuildServiceApplicablility(applicable, message);
  }

  @Override
  public BuildServiceApplicablility checkApplicablility(Project project, ConfigurationSupplier<ImageConfiguration> supplier) {
    if (supplier.isExplicit()) {
      return new BuildServiceApplicablility(true, MESSAGE_OK);
    }

    return checkApplicablility(project, supplier.get());
  }
}
