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

package io.dekorate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import io.dekorate.config.ConfigurationSupplier;
import io.dekorate.kubernetes.config.ImageConfiguration;
import io.dekorate.project.Project;
import io.fabric8.kubernetes.api.model.HasMetadata;

public class LoadServiceFactories {

  private static final Logger LOGGER = LoggerFactory.getLogger();

  public static Optional<LoadServiceFactory> find(Project project, ImageConfiguration config) {
    return stream().filter(f -> f.checkApplicablility(project, config).isApplicable()).sorted()
        .findFirst();
    //    return Optional.empty();
  }

  public static Optional<LoadServiceFactory> find(Project project, ConfigurationSupplier<ImageConfiguration> supplier) {
    return stream().filter(f -> f.checkApplicablility(project, supplier).isApplicable()).sorted()
        .findFirst();
  }

  public static Function<ImageConfiguration, LoadService> create(Project project, Collection<HasMetadata> items) {
    return c -> find(project, c).orElseThrow(() -> new IllegalStateException("No applicable LoadServiceFactory found."))
        .create(project, c);
  }

  public static Predicate<ImageConfiguration> configMatches(Project project) {
    return c -> find(project, c).isPresent();
  }

  public static Predicate<ConfigurationSupplier<ImageConfiguration>> supplierMatches(Project project) {
    return c -> find(project, c).isPresent();
  }

  public static List<String> names() {
    return stream().map(f -> f.name()).collect(Collectors.toList());
  }

  private static Stream<LoadServiceFactory> stream() {
    ServiceLoader<LoadServiceFactory> loader = ServiceLoader.load(LoadServiceFactory.class,
        LoadServiceFactory.class.getClassLoader());
    return StreamSupport.stream(loader.spliterator(), false);
  }

}
