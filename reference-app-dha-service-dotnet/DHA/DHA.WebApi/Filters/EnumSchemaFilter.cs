using Microsoft.OpenApi.Any;
using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;

namespace DHA.WebApi.Filters
{
    //Idea for this filter from this stack overflow post https://stackoverflow.com/questions/65312198/swagger-swashbuckle-asp-net-core-show-details-about-every-enum-is-used
    //the reason is to have mappings for enum values and names
    public class EnumSchemaFilter : ISchemaFilter
    {
        public void Apply(OpenApiSchema schema, SchemaFilterContext context)
        {
            if (context.Type.IsEnum)
            {
                schema.Enum.Clear();
                Enum.GetNames(context.Type)
                .ToList()
                    .ForEach(name => schema.Enum.Add(new OpenApiString($"{Convert.ToInt64(Enum.Parse(context.Type, name))} = {name}")));
            }
        }
    }
}
