# .gitpod.Dockerfile

# Usamos uma imagem base completa do Gitpod
FROM gitpod/workspace-full-vnc

# Troca para o usuário 'gitpod' para instalações não-root
USER gitpod

# Instala o SDKMAN! (gerenciador de versões Java)
# E instala a versão 17 do OpenJDK (Temurin) e a define como padrão
RUN curl -s "https://get.sdkman.io" | bash \
    && bash -c "source ~/.sdkman/bin/sdkman-init.sh && sdk install java 17.0.7-tem && sdk default java 17.0.7-tem"

# Define a variável de ambiente JAVA_HOME
ENV JAVA_HOME="/home/gitpod/.sdkman/candidates/java/current"
ENV PATH="$JAVA_HOME/bin:$PATH"

# Confirma a versão do Java que foi instalada (você verá isso nos logs de construção do workspace)
RUN bash -c "source ~/.sdkman/bin/sdkman-init.sh && java -version"
