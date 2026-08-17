FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
WORKDIR /src
COPY ["SoapApi/SoapApi.csproj", "SoapApi/"]
RUN dotnet restore "SoapApi/SoapApi.csproj"
COPY . .
WORKDIR "/src/SoapApi"
RUN dotnet build "SoapApi.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "SoapApi.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "SoapApi.dll"]