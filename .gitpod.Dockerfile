# .gitpod.Dockerfile

# Usamos uma imagem base completa do Gitpod
FROM gitpod/workspace-full-vnc

# Troca para o usuário 'gitpod' para instalações não-root
USER gitpod

# Instala o SDKMAN! (gerenciador de versões Java)
# E instala a versão 21 do OpenJDK (Temurin) e a define como padrão
RUN curl -s "https://get.sdkman.io" | bash \
    && echo "source ~/.sdkman/bin/sdkman-init.sh" >> ~/.bashrc \
    && sdk install java 21.0.3-tem \
    && sdk default java 21.0.3-tem

# Define a variável de ambiente JAVA_HOME
ENV JAVA_HOME="/home/gitpod/.sdkman/candidates/java/current"
ENV PATH="$JAVA_HOME/bin:$PATH"

# Confirma a versão do Java que foi instalada (você verá isso nos logs de construção do workspace)
RUN java -version