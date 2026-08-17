FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
WORKDIR /src
COPY ["DHA.Application/DHA.Application.csproj", "DHA.Application/"]
COPY ["DHA.Domain/DHA.Domain.csproj", "DHA.Domain/"]
COPY ["DHA.WebApi/DHA.WebApi.csproj", "DHA.WebApi/"]
RUN dotnet restore "DHA.WebApi/DHA.WebApi.csproj"
COPY . .
WORKDIR "/src/DHA.WebApi"
RUN dotnet build "DHA.WebApi.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "DHA.WebApi.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "DHA.WebApi.dll"]